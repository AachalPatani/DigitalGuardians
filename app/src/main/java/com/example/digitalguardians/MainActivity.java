package com.example.digitalguardians;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    // CardView references for the new UI elements
    private CardView cardMessageCheck, cardUrlCheck, cardCallCheck, cardBankTips, cardNewsScanner, cardSettings;
    private Button btnReportFraud, btnChatbot, moretips, checkSimButton,simulateSimSwapButton,resetSimBtn ;
    private TextView statusText;
    private SIMSwapDetector simSwapDetector;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize CardView elements
        cardMessageCheck = findViewById(R.id.yoga_practice);
        cardUrlCheck = findViewById(R.id.url_check);
        cardCallCheck = findViewById(R.id.call_check);
        cardBankTips = findViewById(R.id.bank_tips);
        cardNewsScanner = findViewById(R.id.news_scanner);
        cardSettings = findViewById(R.id.settings);

        // Buttons from the bottom section
        btnReportFraud = findViewById(R.id.btnReportFraud);
        btnChatbot = findViewById(R.id.btnBankChatBot);
        moretips = findViewById(R.id.moretips);
        checkSimButton = findViewById(R.id.checkSimButton);
        statusText = findViewById(R.id.statusText);

        simulateSimSwapButton = findViewById(R.id.simulateSwapButton);
        simulateSimSwapButton.setOnClickListener(v -> {
            simSwapDetector.simulateSimSwap();
            String simStatus = simSwapDetector.checkForSIMChange(); // Now detects fake swap
            statusText.setText(simStatus); // Update TextView
            Toast.makeText(MainActivity.this, "Fake SIM Swap Triggered!", Toast.LENGTH_SHORT).show();
        });

        // Initialize SIMSwapDetector
        simSwapDetector = new SIMSwapDetector(this);

        // Set click listeners for all cards
        cardMessageCheck.setOnClickListener(v -> openManualCheck("sms"));
        cardUrlCheck.setOnClickListener(v -> openManualCheck("url"));
        cardCallCheck.setOnClickListener(v -> openManualCheck("call"));
        cardBankTips.setOnClickListener(v -> startActivity(new Intent(this, BankTipsActivity.class)));
        cardNewsScanner.setOnClickListener(v -> startActivity(new Intent(this, NewsListActivity.class)));
        cardSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        // Set click listeners for buttons
        resetSimBtn = findViewById(R.id.resetSimBtn);
        btnReportFraud.setOnClickListener(v -> startActivity(new Intent(this, ReportFraudActivity.class)));
        btnChatbot.setOnClickListener(v -> startActivity(new Intent(this, ChatActivity.class)));
        moretips.setOnClickListener(v -> startActivity(new Intent(this, MainActivity2.class)));

        // Check SIM Button - Calls SIM Swap Detection
        checkSimButton.setOnClickListener(v -> {
            String simStatus = simSwapDetector.checkForSIMChange(); // Get status
            statusText.setText(simStatus); // Update TextView with status
            Toast.makeText(MainActivity.this, "Checking SIM Security...", Toast.LENGTH_SHORT).show();
        });

        //rest
        resetSimBtn.setOnClickListener(v -> {
            simSwapDetector.resetSimStatus();
            Toast.makeText(this, "SIM Status reset!", Toast.LENGTH_SHORT).show();
        });

    }

    private void openManualCheck(String type) {
        Intent intent = new Intent(this, ManualCheckActivity.class);
        intent.putExtra("type", type); // Pass type to ManualCheckActivity
        startActivity(intent);
    }
}
