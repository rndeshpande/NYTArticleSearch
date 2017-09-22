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

    private static final String baseUrlNytSearch = "https://api.nytimes.com/svc/search/v2/";
    private static final String baseUrlNyt = "http://www.nytimes.com/";
    private static final String API_KEY_NYT = "a7f303d47b8e4c72ba51dd5808d64a31";

    private static final String BEGIN_YEAR = "begin_year";
    private static final String BEGIN_MONTH = "begin_month";
    private static final String BEGIN_DAY = "begin_day";
    private static final String SORT_SELECTED_INDEX = "sortSelectedIndex";
    private static final String SORT_SELECTED_TEXT = "sortSelectedText";
    private static final String IS_CHECKED_ARTS = "isCheckedArts";
    private static final String IS_CHECKED_FASHION = "isCheckedFashion";
    private static final String IS_CHECKED_SPORTS = "isCheckedSports";


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
        String beginYear = sharedpreferences.getString(BEGIN_YEAR, "1851");
        String beginMonth = sharedpreferences.getString(BEGIN_MONTH, "01");
        String beginDay = sharedpreferences.getString(BEGIN_DAY, "01");
        int sortSelectedIndex = sharedpreferences.getInt(SORT_SELECTED_INDEX, 1);
        String sortSelectedText = sharedpreferences.getString(SORT_SELECTED_TEXT, "newest");
        Boolean isCheckedArts = sharedpreferences.getBoolean(IS_CHECKED_ARTS, false);
        Boolean isCheckedFashion = sharedpreferences.getBoolean(IS_CHECKED_FASHION, false);
        Boolean isCheckedSports = sharedpreferences.getBoolean(IS_CHECKED_SPORTS, false);

        return new FilterSettings(beginYear, beginMonth, beginDay, sortSelectedIndex, sortSelectedText, isCheckedArts, isCheckedFashion, isCheckedSports);
    }

    public static void setFilterSettings(Context context, FilterSettings settings) {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(BEGIN_YEAR, settings.getBeginYear());
        editor.putString(BEGIN_MONTH, settings.getBeginMonth());
        editor.putString(BEGIN_DAY, settings.getBeginDay());
        editor.putInt(SORT_SELECTED_INDEX, settings.getSortSelectedIndex());
        editor.putString(SORT_SELECTED_TEXT, settings.getSortSelectedText());
        editor.putBoolean(IS_CHECKED_ARTS, settings.isCheckedArts());
        editor.putBoolean(IS_CHECKED_FASHION, settings.isCheckedFashion());
        editor.putBoolean(IS_CHECKED_SPORTS, settings.isCheckedSports());

        editor.apply();
    }
}
