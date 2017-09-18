package com.codepath.news.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.news.R;
import com.codepath.news.adapters.NewsAdapter;
import com.codepath.news.models.News;
import com.codepath.news.models.NewsSearchResponse;
import com.codepath.news.utils.ConfigHelper;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.rvResults) RecyclerView rvResults;
    RecyclerView.LayoutManager mLayoutManager;
    NewsAdapter adapter;

    ArrayList<News> mNewsItems;
    private final int NUM_OF_COLS = 4;

    private final OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initialize();
        loadContent();
    }

    private void initialize() {
        mNewsItems = new ArrayList<>();
        adapter = new NewsAdapter(this, mNewsItems);

        rvResults.setAdapter(adapter);

        mLayoutManager = new GridLayoutManager(this, NUM_OF_COLS);
        rvResults.setLayoutManager(mLayoutManager);
    }

    private void loadContent() {
        getResponse(ConfigHelper.getSearchUrl());
    }

    private void getResponse(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    final NewsSearchResponse searchResponse = gsonBuilder.create().fromJson(responseBody.string(), NewsSearchResponse.class);
                    final ArrayList<News> resultNews = searchResponse.newsDocs.newsItems;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateDataset(resultNews);
                        }
                    });
                }
            }
        });
    }

    private void updateDataset(ArrayList<News> newsItems) {
        mNewsItems.addAll(newsItems);
        adapter.notifyDataSetChanged();
    }
}
