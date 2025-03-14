package com.example.digitalguardians;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Manually test URL or SMS through backend.
 */
public class ManualCheckActivity extends AppCompatActivity {
    EditText inputText;
    Button btnCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_check);

        inputText = findViewById(R.id.inputText);
        btnCheck = findViewById(R.id.btnCheck);

        // Send manually entered SMS/URL to backend
        btnCheck.setOnClickListener(v -> {
            String message = inputText.getText().toString();

            // ✅ First check as URL if any URL exists
            ApiService.sendUrlToBackend(this, Utils.extractUrl(message));

            // ✅ Also check as SMS Spam
            ApiService.sendSmsToBackend(this, message, "manual");
        });
    }
}
