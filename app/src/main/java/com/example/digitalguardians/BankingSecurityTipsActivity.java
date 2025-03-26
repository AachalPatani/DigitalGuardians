package com.example.digitalguardians;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BankingSecurityTipsActivity extends AppCompatActivity {

    private String[] englishTips = {
            "1. Never share your ATM PIN or online banking password with anyone",
            "2. Bank officials will never call to ask for your OTP or password",
            "3. Always check SMS sender name - real bank messages come from official numbers",
            "4. Before clicking any link, check website address carefully",
            "5. Register for SMS alerts for all bank transactions",
            "6. Change your passwords every 3 months",
            "7. Never use public WiFi for banking transactions",
            "8. If you lose your phone, inform your bank immediately"
    };

    private String[] hindiTips = {
            "1. किसी को भी अपना ATM PIN या बैंकिंग पासवर्ड न बताएं",
            "2. बैंक कर्मचारी कभी भी OTP या पासवर्ड नहीं मांगते",
            "3. हमेशा SMS भेजने वाले का नाम जांचें - असली बैंक संदेश आधिकारिक नंबर से आते हैं",
            "4. किसी लिंक पर क्लिक करने से पहले, वेबसाइट पता अच्छी तरह जांच लें",
            "5. सभी बैंक लेनदेन के लिए SMS अलर्ट के लिए पंजीकरण करें",
            "6. हर 3 महीने में अपना पासवर्ड बदलें",
            "7. बैंकिंग के लिए कभी भी सार्वजनिक WiFi का उपयोग न करें",
            "8. अगर आपका फोन खो जाता है, तुरंत अपने बैंक को सूचित करें"
    };

    private boolean isEnglish = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banking_security_tips);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView tipsRecyclerView = findViewById(R.id.tipsRecyclerView);
        Button toggleLanguageBtn = findViewById(R.id.toggleLanguageBtn);

        // Initial setup with English tips
        updateTipsList(englishTips);

        toggleLanguageBtn.setOnClickListener(v -> {
            isEnglish = !isEnglish;
            if (isEnglish) {
                updateTipsList(englishTips);
                toggleLanguageBtn.setText("हिंदी में देखें");
            } else {
                updateTipsList(hindiTips);
                toggleLanguageBtn.setText("View in English");
            }
        });
    }

    private void updateTipsList(String[] tips) {
        RecyclerView recyclerView = findViewById(R.id.tipsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TipsAdapter(tips));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipViewHolder> {

        private final String[] tips;

        public TipsAdapter(String[] tips) {
            this.tips = tips;
        }

        @NonNull
        @Override
        public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_security_tip, parent, false);
            return new TipViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TipViewHolder holder, int position) {
            holder.tipText.setText(tips[position]);
            holder.itemView.setBackgroundColor(
                    position % 2 == 0 ? Color.parseColor("#FFFFFF") : Color.parseColor("#F5F5F5")
            );
        }

        @Override
        public int getItemCount() {
            return tips.length;
        }

        class TipViewHolder extends RecyclerView.ViewHolder {
            final TextView tipText;

            TipViewHolder(@NonNull View itemView) {
                super(itemView);
                tipText = itemView.findViewById(R.id.tipText);
            }
        }
    }
}