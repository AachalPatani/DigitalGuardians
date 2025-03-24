package com.example.digitalguardians;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.digitalguardians.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Reference to TextView
        TextView splashText = findViewById(R.id.splashText);

        // Load animations
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Start animation
        splashText.startAnimation(zoomIn);

        // Optional: Fade-in combined
        splashText.startAnimation(fadeIn);

        // Go to Main Screen after 3 seconds
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, PermissionsActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN_TIME);
    }
}
