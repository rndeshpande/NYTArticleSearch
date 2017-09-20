package com.codepath.news.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.news.R;
import com.codepath.news.adapters.NewsAdapter;
import com.codepath.news.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.news.models.News;
import com.codepath.news.models.NewsSearchResponse;
import com.codepath.news.utils.ConfigHelper;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Filter;

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

    StaggeredGridLayoutManager mLayoutManager;
    NewsAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    ArrayList<News> mNewsItems;
    private final int NUM_OF_COLS = 4;
    private int mOffset = 0;
    private int mHits = 0;

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

        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(mLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("DATA", "called on load more");
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadContent();
            }
        };
        rvResults.addOnScrollListener(scrollListener);
    }

    private void loadContent() {
        Log.d("DATA", "HITS " + Integer.toString(mHits));
        Log.d("DATA", "OFFSET " + Integer.toString(mOffset));
        if(mOffset <= mHits) {
            getResponse(ConfigHelper.getSearchUrl(this, mOffset));
        }
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
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    final NewsSearchResponse searchResponse = gsonBuilder.create().fromJson(responseBody.string(), NewsSearchResponse.class);
                    final ArrayList<News> resultNews = searchResponse.newsDocs.newsItems;
                    mOffset = searchResponse.newsDocs.meta.offset;
                    mHits = searchResponse.newsDocs.meta.hits;

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
        int position = mNewsItems.size();
        mNewsItems.addAll(newsItems);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem filterItem = menu.findItem(R.id.action_filter);
        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                startActivity(intent);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }
}
