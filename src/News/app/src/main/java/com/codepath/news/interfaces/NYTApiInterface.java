package com.codepath.news.interfaces;

import com.codepath.news.models.NewsSearchResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by rdeshpan on 9/21/2017.
 */

public interface NYTApiInterface {
    @GET("articlesearch.json")
    Call<NewsSearchResponse> getSearchResults(@Query("fq") String userSearchText, @QueryMap Map<String, String> options, @Query("fq") String source, @Query("page") int page, @Query("api_key") String apiKey);
}
