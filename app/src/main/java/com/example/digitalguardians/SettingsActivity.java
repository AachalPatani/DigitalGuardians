package com.example.digitalguardians;


import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.digitalguardians.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.btnFaq).setOnClickListener(v ->
                Toast.makeText(this, "FAQs will be added soon!", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnVoiceAssist).setOnClickListener(v ->
                Toast.makeText(this, "Voice assistance feature coming soon!", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnNotifications).setOnClickListener(v ->
                Toast.makeText(this, "Manage notifications feature coming soon!", Toast.LENGTH_SHORT).show());
    }
}
