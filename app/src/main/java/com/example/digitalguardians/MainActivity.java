package com.example.digitalguardians;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Main home screen with manual check feature.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnManualCheck = findViewById(R.id.btnManualCheck);

        // Navigate to manual check screen
        btnManualCheck.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ManualCheckActivity.class);
            startActivity(intent);
        });
    }
} 