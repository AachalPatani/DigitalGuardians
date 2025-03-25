package com.example.digitalguardians;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    // CardView references for the new UI elements
    private CardView cardMessageCheck, cardUrlCheck, cardCallCheck, cardBankTips, cardNewsScanner, cardSettings;
    private Button btnReportFraud, btnChatbot,moretips;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the CardView elements from the new layout
        cardMessageCheck = findViewById(R.id.yoga_practice);
        cardUrlCheck = findViewById(R.id.url_check);
        cardCallCheck = findViewById(R.id.call_check);
        cardBankTips = findViewById(R.id.bank_tips);
        cardNewsScanner = findViewById(R.id.news_scanner);
        cardSettings = findViewById(R.id.settings);

        // Buttons from the bottom section
        btnReportFraud = findViewById(R.id.btnReportFraud);
        btnChatbot = findViewById(R.id.btnBankChatBot);
        moretips=findViewById(R.id.moretips);

        // Set click listeners for all cards
        cardMessageCheck.setOnClickListener(v -> openManualCheck("sms"));
        cardUrlCheck.setOnClickListener(v -> openManualCheck("url"));
        cardCallCheck.setOnClickListener(v -> openManualCheck("call"));
        cardBankTips.setOnClickListener(v -> startActivity(new Intent(this, BankTipsActivity.class)));
        cardNewsScanner.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewsListActivity.class);
            startActivity(intent);
        });
        cardSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        // Set click listeners for buttons
        btnReportFraud.setOnClickListener(v -> startActivity(new Intent(this, ReportFraudActivity.class)));
        btnChatbot.setOnClickListener(v -> startActivity(new Intent(this, ChatActivity.class)));
        moretips.setOnClickListener(v -> startActivity(new Intent(this, MainActivity2.class)));
    }

    private void openManualCheck(String type) {
        Intent intent = new Intent(this, ManualCheckActivity.class);
        intent.putExtra("type", type); // Pass type to ManualCheckActivity
        startActivity(intent);
    }
}