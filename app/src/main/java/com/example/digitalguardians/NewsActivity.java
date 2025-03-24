package com.example.digitalguardians;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NewsActivity extends AppCompatActivity {

    TextView newsTitle, newsDescription;
    Button openLinkButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        newsTitle = findViewById(R.id.newsTitle);
        newsDescription = findViewById(R.id.newsDescription);
        openLinkButton = findViewById(R.id.openLinkButton);

        // Get data from Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("newsTitle");
        String description = intent.getStringExtra("newsDescription");
        String link = intent.getStringExtra("newsLink");

        // Set data
        newsTitle.setText(title);
        newsDescription.setText(description);

        // Open the news link in a browser
        openLinkButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(browserIntent);
        });
    }
}
