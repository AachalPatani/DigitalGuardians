package com.example.digitalguardians;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Splash screen that shows the app name for 3 seconds before moving to permission check.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Move to permission screen after 3 seconds
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, PermissionsActivity.class);
            startActivity(intent);
            finish(); // Close splash screen
        }, 3000);
    }
}
