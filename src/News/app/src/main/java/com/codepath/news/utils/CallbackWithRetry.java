package com.codepath.news.utils;

import android.util.Log;

import com.codepath.news.R;
import com.codepath.news.models.News;
import com.codepath.news.models.NewsSearchResponse;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rdeshpan on 9/24/2017.
 */

public abstract class CallbackWithRetry<T> implements Callback<T> {

    private static final int TOTAL_RETRIES = 3;
    private static final String TAG = CallbackWithRetry.class.getSimpleName();
    private final Call<T> call;
    private int retryCount = 0;
    private final static int TOO_MANY_REQUESTS = 429;

    public CallbackWithRetry(Call<T> call) {
        this.call = call;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.code() == TOO_MANY_REQUESTS)
            retry();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        retry();
    }

    public void retry() {
        if (retryCount++ < TOTAL_RETRIES) {
            Log.v(TAG, "Retrying... (" + retryCount + " out of " + TOTAL_RETRIES + ")");
            
            call.clone().enqueue(this);
        }
    }
}
