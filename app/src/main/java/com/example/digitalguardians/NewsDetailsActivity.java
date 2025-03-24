package com.example.digitalguardians;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NewsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        TextView titleTextView = findViewById(R.id.newsTitle);
        TextView descTextView = findViewById(R.id.newsDescription);
        Button readFullArticleBtn = findViewById(R.id.btnReadFullArticle);

        Intent intent = getIntent();
        String title = intent.getStringExtra("newsTitle");
        String description = intent.getStringExtra("newsDescription");
        String link = intent.getStringExtra("newsLink");

        titleTextView.setText(title);
        descTextView.setText(description);

        readFullArticleBtn.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(browserIntent);
        });
    }
}
