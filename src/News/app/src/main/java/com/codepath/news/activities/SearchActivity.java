package com.codepath.news.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.news.FilterDialogFragment;
import com.codepath.news.R;
import com.codepath.news.adapters.NewsAdapter;
import com.codepath.news.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.news.models.FilterSettings;
import com.codepath.news.models.News;
import com.codepath.news.models.NewsSearchResponse;
import com.codepath.news.utils.CommonHelper;
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

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.OnFragmentInteractionListener {

    @BindView(R.id.rvResults) RecyclerView rvResults;

    StaggeredGridLayoutManager mLayoutManager;
    NewsAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private FragmentManager fragManager;

    ArrayList<News> mNewsItems;
    private final int NUM_OF_COLS = 4;
    private int mOffset = 0;
    private int mHits = 0;
    private String mUserSearch= "";

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
                loadContent();
            }
        };
        rvResults.addOnScrollListener(scrollListener);
    }

    private void loadContent() {
        if(mOffset <= mHits) {
            getResponse(CommonHelper.getSearchUrl(this, mOffset,mUserSearch));
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
                resetSearch();
                mUserSearch = query;
                loadContent();
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
                FilterSettings settings = CommonHelper.getFilterSettings(getApplicationContext());
                FilterDialogFragment dialogFragment = FilterDialogFragment.newInstance(settings.getBeginYear(), settings.getBeginMonth(), settings.getBeginDay(), settings.getSortSelectedIndex(),settings.isCheckedArts(),settings.isCheckedFashion(),settings.isCheckedSports());
                dialogFragment.show(getSupportFragmentManager(), "fragment_filter_dialog");
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onFragmentInteraction(String beginYear, String beginMonth, String beginDay, int sortSelectedIndex, String sortSelectedText, boolean isCheckedArts, boolean isCheckedFashion, boolean isCheckedSports) {
        Toast.makeText(this, "Settings applied", Toast.LENGTH_SHORT).show();
        CommonHelper.setFilterSettings(getApplicationContext(), beginYear, beginMonth, beginDay, sortSelectedIndex, sortSelectedText, isCheckedArts, isCheckedFashion, isCheckedSports);

        // Reset search params and trigger a fresh search
        resetSearch();
        loadContent();
    }

    private void resetSearch() {
        mHits = 0;
        mOffset = 0;
        mNewsItems.clear();
        adapter.notifyDataSetChanged();
    }
}
