package com.codepath.news.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.codepath.news.models.FilterSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rdeshpan on 9/18/2017.
 */

public class CommonHelper {

    //private static final String baseUrlNytSearch = "https://api.nytimes.com/svc/search/v2/articlesearch.json/";
    private static final String baseUrlNytSearch = "https://api.nytimes.com/svc/search/v2/";
    private static final String baseUrlNyt = "http://www.nytimes.com/";
    private static final String API_KEY_NYT = "a7f303d47b8e4c72ba51dd5808d64a31";

    public static String getBaseUrlNytSearch() {
        return  baseUrlNytSearch;
    }

    public static String getApiKeyNyt() {
        return API_KEY_NYT;
    }

    public static String getDataSource() {
        return "source:(\"The New York Times\")";
    }

    public static String getImageUrl(String relativePath) {
        return baseUrlNyt + relativePath;
    }

    public static Map<String, String> getQueryParams(Context context) {
        FilterSettings settings = getFilterSettings(context);
        Map<String, String> map = new HashMap<>();

        String beginMonth = settings.getBeginMonth().length() > 1 ? settings.getBeginMonth() : "0" + settings.getBeginMonth();
        String beginDay = settings.getBeginDay().length() > 1 ? settings.getBeginDay() : "0" + settings.getBeginDay();
        String beginDate = settings.getBeginYear() + beginMonth + beginDay;
        map.put("begin_date", beginDate);
        map.put("sort", settings.getSortSelectedText());

        StringBuilder news_desk = new StringBuilder();

        if (settings.isCheckedArts())
            news_desk.append("\"Arts\" ");

        if (settings.isCheckedFashion())
            news_desk.append("\"Fashion & Style\" ");

        if (settings.isCheckedSports())
            news_desk.append("\"Sports\" ");

        map.put("fq","news_desk:(" + news_desk.toString().trim() + ")");
        return map;
    }

    public static String getUserSearchParam(String userSearchText) {
        String searchParam = null;
        if (userSearchText != "") {
            searchParam = "\"" + userSearchText + "\"";
        }
        return searchParam;
    }

    public static int getPageParams(int offset) {
        return offset/10;
    }

    public static FilterSettings getFilterSettings(Context context) {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String beginYear = sharedpreferences.getString("begin_year", "1851");
        String beginMonth = sharedpreferences.getString("begin_month", "01");
        String beginDay = sharedpreferences.getString("begin_day", "01");
        int sortSelectedIndex = sharedpreferences.getInt("sortSelectedIndex", 1);
        String sortSelectedText = sharedpreferences.getString("sortSelectedText", "newest");
        Boolean isCheckedArts = sharedpreferences.getBoolean("isCheckedArts", false);
        Boolean isCheckedFashion = sharedpreferences.getBoolean("isCheckedFashion", false);
        Boolean isCheckedSports = sharedpreferences.getBoolean("isCheckedSports", false);

        return new FilterSettings(beginYear, beginMonth, beginDay, sortSelectedIndex, sortSelectedText, isCheckedArts, isCheckedFashion, isCheckedSports);
    }

    public static void setFilterSettings(Context context, FilterSettings settings) {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("begin_year", settings.getBeginYear());
        editor.putString("begin_month", settings.getBeginMonth());
        editor.putString("begin_day", settings.getBeginDay());
        editor.putInt("sortSelectedIndex", settings.getSortSelectedIndex());
        editor.putString("sortSelectedText", settings.getSortSelectedText());
        editor.putBoolean("isCheckedArts", settings.isCheckedArts());
        editor.putBoolean("isCheckedFashion", settings.isCheckedFashion());
        editor.putBoolean("isCheckedSports", settings.isCheckedSports());

        editor.apply();
    }
}
