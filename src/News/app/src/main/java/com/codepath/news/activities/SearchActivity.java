package com.codepath.news.activities;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ConfigurationHelper;
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
import android.widget.Toast;

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
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/*import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;*/

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

    //private final OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);

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
                }
                else {
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
                FilterDialogFragment dialogFragment = FilterDialogFragment.newInstance(settings);
                dialogFragment.show(getSupportFragmentManager(), "fragment_filter_dialog");
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onFragmentInteraction(FilterSettings settings) {
        Toast.makeText(this, "Settings applied", Toast.LENGTH_SHORT).show();
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
