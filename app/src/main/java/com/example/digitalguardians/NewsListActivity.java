package com.example.digitalguardians;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NewsListActivity extends AppCompatActivity {

    RecyclerView newsRecyclerView;
    NewsAdapter newsAdapter;
    List<NewsItem> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, newsList);
        newsRecyclerView.setAdapter(newsAdapter);

        fetchNews();
    }

    private void fetchNews() {
        NewsFetcher.fetchNews(newsList, newsAdapter);
    }
}
