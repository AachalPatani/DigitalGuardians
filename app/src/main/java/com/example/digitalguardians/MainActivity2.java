package com.example.digitalguardians;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

public class MainActivity2 extends AppCompatActivity implements OTPReceiver.OTPListener {
    private TextView otpText, statusText;
    private Button decryptOtpButton;
    private String encryptedOTP = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        otpText = findViewById(R.id.otpText);
        statusText = findViewById(R.id.statusText);
        decryptOtpButton = findViewById(R.id.decryptOtpButton);

        OTPReceiver.setOTPListener(this); // ✅ Auto-capture OTP

        decryptOtpButton.setOnClickListener(v -> {
            if (encryptedOTP == null || encryptedOTP.isEmpty()) {
                Toast.makeText(this, "No OTP to decrypt!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                String decryptedOTP = SecureOTPHandler.decryptOTP(encryptedOTP);
                Log.d("MainActivity2", "Decrypted OTP: " + decryptedOTP);
                statusText.setText("Decrypted OTP: " + decryptedOTP);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Decryption failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onOTPReceived(String otp) {
        runOnUiThread(() -> {
            otpText.setText("Received OTP: " + otp);
            try {
                encryptedOTP = SecureOTPHandler.encryptOTP(otp);
                statusText.setText("Encrypted OTP: " + encryptedOTP);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity2.this, "Encryption failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
