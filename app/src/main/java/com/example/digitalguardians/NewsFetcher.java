package com.example.digitalguardians;

import android.os.AsyncTask;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.List;

public class NewsFetcher {

    public static void fetchNews(List<NewsItem> newsList, NewsAdapter newsAdapter) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    String url = "https://news.google.com/rss/search?q=bank+scam+fraud";

                    // Fetch and parse the RSS feed using Jsoup
                    Document doc = Jsoup.connect(url).get();
                    Elements items = doc.select("item");

                    newsList.clear();  // Clear old news
                    for (Element item : items) {
                        String title = item.select("title").text();
                        String link = item.select("link").text();
                        newsList.add(new NewsItem(title, link));

                        if (newsList.size() >= 10) break;
                    }
                } catch (Exception e) {
                    Log.e("NewsFetcher", "Error fetching news", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                newsAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
