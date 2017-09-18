package com.codepath.news.utils;

/**
 * Created by rdeshpan on 9/18/2017.
 */

public class ConfigHelper {

    private static final String baseUrlNytSearch = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private static final String baseUrlNyt = "http://www.nytimes.com/";
    private static final String API_KEY_NYT = "a7f303d47b8e4c72ba51dd5808d64a31";

    public static String getSearchUrl() {
        return baseUrlNytSearch + getSearchParams() + "&api_key=" + API_KEY_NYT;
    }

    public static String getImageUrl(String relativePath) {
        return baseUrlNyt + relativePath;
    }

    private static String getSearchParams() {
        return "?begin_date=20160112&sort=oldest&fq=news_desk:(%22Education%22%20%22Health%22)";
    }
}
