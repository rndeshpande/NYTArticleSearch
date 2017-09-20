package com.codepath.news.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.codepath.news.models.FilterSettings;

/**
 * Created by rdeshpan on 9/18/2017.
 */

public class ConfigHelper {

    private static final String baseUrlNytSearch = "https://api.nytimes.com/svc/search/v2/articlesearch.json?";
    private static final String baseUrlNyt = "http://www.nytimes.com/";
    private static final String API_KEY_NYT = "a7f303d47b8e4c72ba51dd5808d64a31";

    public static String getSearchUrl(Context context, int offset, String userSearchText) {
        String filterParams = getFilterQuery(context, userSearchText);
        String page = Integer.toString((offset/10) + 1);
        String url = baseUrlNytSearch + filterParams + "&page=" + page + "&api_key=" + API_KEY_NYT;

        Log.d("DATA", url);
        return url;
    }

    public static String getImageUrl(String relativePath) {
        return baseUrlNyt + relativePath;
    }

    private static String getSearchParams() {
        return "?begin_date=20160112&sort=oldest&fq=news_desk:(%22Education%22%20%22Health%22)";
    }

    private static String getFilterQuery(Context context, String userSearchText) {
        FilterSettings settings = getFilterSettings(context);

        StringBuilder query = new StringBuilder();

        query.append("begin_date=").append(settings.getBeginDate())
                .append("&sort=").append(settings.getSortSelectedText())
                .append("&fq=source:(\"The New York Times\")");

        if (userSearchText != "")
                query.append("&fq=(\"").append(userSearchText).append("\")");


        StringBuilder news_desk = new StringBuilder();

        if (settings.isCheckedArts())
            news_desk.append("\"Arts\" ");

        if (settings.isCheckedFashion())
            news_desk.append("\"Fashion & Style\" ");

        if (settings.isCheckedSports())
            news_desk.append("\"Sports\" ");

        if (news_desk.toString() != "")
            query.append("&fq=news_desk(").append(news_desk).append(")");

        return query.toString();
    }

    public static FilterSettings getFilterSettings(Context context) {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int sortSelectedIndex = sharedpreferences.getInt("sortSelectedIndex", 1);
        String sortSelectedText = sharedpreferences.getString("sortSelectedText", "newest");
        Boolean isCheckedArts = sharedpreferences.getBoolean("isCheckedArts", false);
        Boolean isCheckedFashion = sharedpreferences.getBoolean("isCheckedFashion", false);
        Boolean isCheckedSports = sharedpreferences.getBoolean("isCheckedSports", false);
        String beginDate = sharedpreferences.getString("begin_date", "18510101");

        return new FilterSettings(beginDate, sortSelectedIndex, sortSelectedText, isCheckedArts, isCheckedFashion, isCheckedSports);
    }

    public static void setFilterSettings(Context context, String beginDate, int sortSelectedIndex, String sortSelectedText, boolean isCheckedArts, boolean isCheckedFashion, boolean isCheckedSports) {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("begin_date", beginDate);
        editor.putInt("sortSelectedIndex", sortSelectedIndex);
        editor.putString("sortSelectedText", sortSelectedText);
        editor.putBoolean("isCheckedArts", isCheckedArts);
        editor.putBoolean("isCheckedFashion", isCheckedFashion);
        editor.putBoolean("isCheckedSports", isCheckedSports);

        editor.apply();
    }
}
