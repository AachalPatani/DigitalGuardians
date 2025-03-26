package com.example.digitalguardians;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BankingAssistanceActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private boolean isEnglish = true;
    private List<FAQItem> englishFaqs = new ArrayList<>();
    private List<FAQItem> hindiFaqs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banking_assistance);

        // Initialize TTS
        textToSpeech = new TextToSpeech(this, this);

        setupToolbar();
        initializeFAQs();
        setupViews();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Banking Assistance");
        }
    }

    private void initializeFAQs() {
        // English FAQs
        englishFaqs.add(new FAQItem("What should I do if I shared my OTP with someone?",
                "Immediately contact your bank to block your account. Change all passwords and enable transaction alerts."));
        englishFaqs.add(new FAQItem("How to identify fake bank messages?",
                "Check sender number (real banks use official shortcodes), look for spelling mistakes, and never click links in unexpected messages."));
        englishFaqs.add(new FAQItem("Is it safe to use mobile banking?",
                "Yes, if you: 1) Use official bank app 2) Never share login details 3) Enable app lock 4) Logout after use."));

        // Hindi FAQs
        hindiFaqs.add(new FAQItem("अगर मैंने किसी को अपना OTP दे दिया तो क्या करूँ?",
                "तुरंत अपने बैंक को संपर्क करें, अपना खाता ब्लॉक करवाएं। सभी पासवर्ड बदलें और लेनदेन अलर्ट सक्षम करें।"));
        hindiFaqs.add(new FAQItem("नकली बैंक संदेशों की पहचान कैसे करें?",
                "भेजने वाले नंबर जांचें (असली बैंक आधिकारिक नंबरों से भेजते हैं), वर्तनी की गलतियाँ देखें, और अप्रत्याशित संदेशों में लिंक पर कभी क्लिक न करें।"));
        hindiFaqs.add(new FAQItem("क्या मोबाइल बैंकिंग सुरक्षित है?",
                "हाँ, अगर आप: 1) आधिकारिक बैंक ऐप इस्तेमाल करें 2) लॉगिन विवरण कभी न बताएँ 3) ऐप लॉक सक्षम करें 4) उपयोग के बाद लॉगआउट करें।"));
    }

    private void setupViews() {
        Button btnFaq = findViewById(R.id.btnFaq);
        Button btnVoiceAssist = findViewById(R.id.btnVoiceAssist);
        Button btnNotifications = findViewById(R.id.btnNotifications);
        Button btnToggleLanguage = findViewById(R.id.btnToggleLanguage);

        RecyclerView faqRecyclerView = findViewById(R.id.faqRecyclerView);
        faqRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        faqRecyclerView.setAdapter(new FAQAdapter(englishFaqs));

        btnFaq.setOnClickListener(v -> showFAQs());
        btnVoiceAssist.setOnClickListener(v -> startVoiceAssistance());
        btnNotifications.setOnClickListener(v -> {
            // Create a basic notification settings activity intent
            Intent intent = new Intent(BankingAssistanceActivity.this, SettingsActivity.class);
            intent.putExtra("settings_type", "notifications");
            startActivity(intent);
        });
        btnToggleLanguage.setOnClickListener(v -> toggleLanguage());
    }

    private void showFAQs() {
        RecyclerView recyclerView = findViewById(R.id.faqRecyclerView);
        FAQAdapter adapter = (FAQAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.updateFAQs(isEnglish ? englishFaqs : hindiFaqs);
        }
    }

    private void startVoiceAssistance() {
        if (textToSpeech != null) {
            String text = isEnglish ?
                    "Banking security tips. Never share your passwords. Always verify transactions." :
                    "बैंकिंग सुरक्षा सुझाव। अपने पासवर्ड कभी न बताएं। लेनदेन हमेशा सत्यापित करें।";
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void toggleLanguage() {
        isEnglish = !isEnglish;
        Button btnToggleLanguage = findViewById(R.id.btnToggleLanguage);
        btnToggleLanguage.setText(isEnglish ? "हिंदी में देखें" : "View in English");
        showFAQs();
        Toast.makeText(this, isEnglish ? "Language set to English" : "भाषा हिंदी में सेट की गई", Toast.LENGTH_SHORT).show();

        // Update TTS language
        if (textToSpeech != null) {
            int result = textToSpeech.setLanguage(isEnglish ? Locale.US : new Locale("hi"));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(isEnglish ? Locale.US : new Locale("hi"));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {
        private List<FAQItem> faqItems;

        public FAQAdapter(List<FAQItem> faqItems) {
            this.faqItems = faqItems;
        }

        public void updateFAQs(List<FAQItem> newFaqItems) {
            this.faqItems = newFaqItems;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_faq, parent, false);
            return new FAQViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
            FAQItem item = faqItems.get(position);
            holder.question.setText(item.getQuestion());
            holder.answer.setText(item.getAnswer());

            // Alternate background colors
            holder.itemView.setBackgroundColor(
                    position % 2 == 0 ? Color.parseColor("#FFFFFF") : Color.parseColor("#F5F5F5")
            );
        }

        @Override
        public int getItemCount() {
            return faqItems.size();
        }

        class FAQViewHolder extends RecyclerView.ViewHolder {
            TextView question, answer;

            FAQViewHolder(View itemView) {
                super(itemView);
                question = itemView.findViewById(R.id.tvQuestion);
                answer = itemView.findViewById(R.id.tvAnswer);
            }
        }
    }

    private static class FAQItem {
        private final String question;
        private final String answer;

        public FAQItem(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }
    }
}