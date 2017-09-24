package com.codepath.news.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.news.databinding.ActivitySearchBinding;
import com.codepath.news.fragments.FilterDialogFragment;
import com.codepath.news.R;
import com.codepath.news.adapters.NewsAdapter;
import com.codepath.news.interfaces.NYTApiInterface;
import com.codepath.news.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.news.models.FilterSettings;
import com.codepath.news.models.News;
import com.codepath.news.models.NewsSearchResponse;
import com.codepath.news.utils.CommonHelper;
import com.facebook.stetho.Stetho;

import com.google.gson.GsonBuilder;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.OnFragmentInteractionListener {

    RecyclerView rvResults;
    private ActivitySearchBinding binding;

    StaggeredGridLayoutManager mLayoutManager;
    NewsAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private FragmentManager fragManager;

    ArrayList<News> mNewsItems;
    private final int NUM_OF_COLS = 4;
    private int mOffset = 0;
    private int mHits = 0;
    private String mUserSearch = "";

    private final static String TAG = "Search";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
        loadContent();
    }

    private void initialize() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        Stetho.initializeWithDefaults(this);
        mNewsItems = new ArrayList<>();
        adapter = new NewsAdapter(this, mNewsItems);
        rvResults = binding.rvResults;

        rvResults.setAdapter(adapter);

        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(mLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // TODO : use the page, itemscount and other parameters to load the next content
                loadContent();
            }
        };
        rvResults.addOnScrollListener(scrollListener);
    }

    private void loadContent() {
        if (mOffset <= mHits) {
            getResponse();
        }
    }

    private void getResponse() {
        //if (isNetworkAvailable() && isOnline()) {
        if (true) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(CommonHelper.getBaseUrlNytSearch())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            NYTApiInterface apiInterface = retrofit.create(NYTApiInterface.class);

            Call<NewsSearchResponse> call = apiInterface.getSearchResults(
                    CommonHelper.getUserSearchParam(mUserSearch),
                    CommonHelper.getQueryParams(this),
                    CommonHelper.getDataSource(),
                    CommonHelper.getPageParams(mOffset),
                    CommonHelper.getApiKeyNyt()
            );

            call.enqueue(new Callback<NewsSearchResponse>() {
                @Override
                public void onResponse(Call<NewsSearchResponse> call, Response<NewsSearchResponse> response) {
                    if (!response.isSuccessful()) {
                        showMessage(getString(R.string.generic_failure_message));
                    } else {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        final NewsSearchResponse searchResponse = response.body();
                        final ArrayList<News> resultNews = searchResponse.newsDocs.newsItems;
                        mOffset = searchResponse.newsDocs.meta.offset;
                        mHits = searchResponse.newsDocs.meta.hits;
                        updateDataset(resultNews);
                    }
                }

                @Override
                public void onFailure(Call<NewsSearchResponse> call, Throwable t) {
                    showMessage(getString(R.string.generic_failure_message));
                }
            });
        } else {
            showMessage(getString(R.string.internet_unavailable_message));
        }
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
                mUserSearch = newText;
                return false;
            }
        });

        MenuItem filterItem = menu.findItem(R.id.action_filter);

        filterItem.setOnMenuItemClickListener(v -> {
            FilterSettings settings = CommonHelper.getFilterSettings(SearchActivity.this.getApplicationContext());
            FilterDialogFragment dialogFragment = FilterDialogFragment.newInstance(Parcels.wrap(settings));
            dialogFragment.show(SearchActivity.this.getSupportFragmentManager(), "fragment_filter_dialog");
            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onFragmentInteraction(FilterSettings settings) {
        showMessage(getString(R.string.status_message));
        CommonHelper.setFilterSettings(getApplicationContext(), settings);

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

    private void showMessage(String message) {
        Snackbar.make(this.findViewById(R.id.clMain), message, Snackbar.LENGTH_LONG).show();
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


}
