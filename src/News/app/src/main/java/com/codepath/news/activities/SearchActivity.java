package com.codepath.news.activities;

import android.databinding.DataBindingUtil;
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
                loadContent();
            }
        };
        rvResults.addOnScrollListener(scrollListener);
    }

    private void loadContent() {
        if (mOffset <= mHits) {
            Log.d("DATA", "getting data");
            getResponse();
        }
    }

    private void getResponse() {
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
                    Log.d("DATA", Integer.toString(response.code()));
                } else {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    final NewsSearchResponse searchResponse = response.body();
                    final ArrayList<News> resultNews = searchResponse.newsDocs.newsItems;
                    mOffset = searchResponse.newsDocs.meta.offset;
                    mHits = searchResponse.newsDocs.meta.hits;
                    Log.d("DATA", "results fetched");
                    updateDataset(resultNews);
                }
            }

            @Override
            public void onFailure(Call<NewsSearchResponse> call, Throwable t) {
                //TODO : Add error handling
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
        Snackbar.make(this.findViewById(R.id.clMain), "Settings Updated", Snackbar.LENGTH_LONG).show();
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
}
