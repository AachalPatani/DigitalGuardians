package com.example.digitalguardians;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ManualCheckActivity extends AppCompatActivity {

    private EditText inputText;
    private Button btnCheck;
    private TextView titleText;
    private String checkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_check);

        // ✅ Initialize UI elements
        inputText = findViewById(R.id.inputText);
        btnCheck = findViewById(R.id.btnCheck);
        titleText = findViewById(R.id.titleText);

        // ✅ Retrieve "type" from Intent with null safety
        checkType = getIntent().getStringExtra("type");
        if (checkType == null) {
            checkType = "unknown";
            Log.e("DEBUG", "❌ Received NULL type in ManualCheckActivity");
        } else {
            Log.d("DEBUG", "✅ Received type: " + checkType);
        }

        // ✅ Set dynamic title
        switch (checkType) {
            case "sms":
                titleText.setText("Check SMS");
                break;
            case "url":
                titleText.setText("Check URL");
                break;
            case "call":
                titleText.setText("Check Call");
                break;
            default:
                titleText.setText("Check Content");
                break;
        }

        // ✅ Button Click Listener
        btnCheck.setOnClickListener(v -> {
            String input = inputText.getText().toString().trim();
            if (TextUtils.isEmpty(input)) {
                Toast.makeText(this, "❌ Please enter a valid input!", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("sms".equals(checkType)) {
                Log.d("DEBUG", "📨 Checking SMS...");

                ApiService.sendSmsToBackend(this, input, "manual");
            } else if ("url".equals(checkType)) {
                Log.d("DEBUG", "🌐 Checking URL...");

                ApiService.sendUrlToBackend(this, input);
            } else if ("call".equals(checkType)) {
                Log.d("DEBUG", "📞 Checking Call (Future Implementation)...");
                ApiService.sendSmsToBackend(this, input, "call-manual"); // Placeholder usage
            } else {
                Toast.makeText(this, "⚠️ Invalid Check Type!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
