package com.codepath.news.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;

import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.codepath.news.databinding.ActivitySearchBinding;
import com.codepath.news.fragments.FilterDialogFragment;
import com.codepath.news.R;
import com.codepath.news.adapters.NewsAdapter;
import com.codepath.news.interfaces.NYTApiInterface;
import com.codepath.news.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.news.models.FilterSettings;
import com.codepath.news.models.News;
import com.codepath.news.models.NewsSearchResponse;
import com.codepath.news.utils.CallbackWithRetry;
import com.codepath.news.utils.CommonHelper;
import com.facebook.stetho.Stetho;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.GsonBuilder;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.OnFragmentInteractionListener {

    RecyclerView rvResults;
    SwipeRefreshLayout swipeContainer;
    ProgressBar pbLoading;
    private ActivitySearchBinding binding;

    StaggeredGridLayoutManager mLayoutManager;
    NewsAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    ArrayList<News> mNewsItems;
    private final int NUM_OF_COLS = 2;
    private int mPage = 0;
    private String mUserSearch = "";
    private final static String TAG = "CODEPATH_NYTSEARCH";
    private final static int RESPONSE_DELAY = 300;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        loadContent(true);
    }

    private void initialize() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        Stetho.initializeWithDefaults(this);

        pbLoading = binding.pbLoading;
        pbLoading.setVisibility(ProgressBar.VISIBLE);

        mNewsItems = new ArrayList<>();
        adapter = new NewsAdapter(this, mNewsItems);
        rvResults = binding.rvResults;

        rvResults.setAdapter(adapter);

        mLayoutManager = new StaggeredGridLayoutManager(NUM_OF_COLS, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(mLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mPage = page;
                loadContent(false);
            }
        };
        rvResults.addOnScrollListener(scrollListener);

        swipeContainer = binding.swipeContainer;
        swipeContainer.setOnRefreshListener(() -> {
            resetSearch();
            loadContent(false);
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void loadContent(boolean showProgress) {
        pbLoading.setVisibility(showProgress ? ProgressBar.VISIBLE: ProgressBar.INVISIBLE);
        getResponse();
    }

    private void getResponse() {
        if (true) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addNetworkInterceptor(chain -> {
                        SystemClock.sleep(RESPONSE_DELAY);
                        return chain.proceed(chain.request());
                    })
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(CommonHelper.getBaseUrlNytSearch())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            NYTApiInterface apiInterface = retrofit.create(NYTApiInterface.class);

            Call<NewsSearchResponse> call = apiInterface.getSearchResults(
                    CommonHelper.getUserSearchParam(mUserSearch),
                    CommonHelper.getQueryParams(this),
                    CommonHelper.getDataSource(),
                    mPage,
                    CommonHelper.getApiKeyNyt()
            );


            Callback<NewsSearchResponse> callback = new CallbackWithRetry<NewsSearchResponse>(call) {
                @Override
                public void onResponse(Call<NewsSearchResponse> call, Response<NewsSearchResponse> response) {
                    super.onResponse(call, response);

                    if (response.isSuccessful()) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        final NewsSearchResponse searchResponse = response.body();
                        final ArrayList<News> resultNews = searchResponse.newsDocs.newsItems;
                        updateDataset(resultNews);
                        pbLoading.setVisibility(ProgressBar.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<NewsSearchResponse> call, Throwable t) {
                    super.onFailure(call, t);
                    showMessage(getString(R.string.generic_failure_message));
                    pbLoading.setVisibility(ProgressBar.INVISIBLE);
                }
            };

            call.enqueue(callback);
        } else {
            showMessage(getString(R.string.internet_unavailable_message));
            pbLoading.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    private void updateDataset(ArrayList<News> newsItems) {
        int positionStart = mNewsItems.size();
        mNewsItems.addAll(newsItems);
        adapter.notifyItemRangeInserted(positionStart, newsItems.size());
        swipeContainer.setRefreshing(false);
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
                loadContent(true);
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
        loadContent(true);
    }

    private void resetSearch() {
        mPage = 0;
        int size = mNewsItems.size();
        mNewsItems.clear();
        adapter.notifyItemRangeRemoved(0, size);
        scrollListener.resetState();
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
