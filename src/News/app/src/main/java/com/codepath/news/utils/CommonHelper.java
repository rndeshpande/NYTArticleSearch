package com.codepath.news.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.codepath.news.models.FilterSettings;

/**
 * Created by rdeshpan on 9/18/2017.
 */

public class CommonHelper {

    private static final String baseUrlNytSearch = "https://api.nytimes.com/svc/search/v2/articlesearch.json?";
    private static final String baseUrlNyt = "http://www.nytimes.com/";
    private static final String API_KEY_NYT = "a7f303d47b8e4c72ba51dd5808d64a31";

    public static String getSearchUrl(Context context, int offset, String userSearchText) {
        String filterParams = getFilterQuery(context, userSearchText);
        String page = Integer.toString(offset/10);
        String url = baseUrlNytSearch + filterParams + "&page=" + page + "&api_key=" + API_KEY_NYT;

        Log.d("DATA", url);
        return url;
    }

    public static String getImageUrl(String relativePath) {
        return baseUrlNyt + relativePath;
    }

    private static String getFilterQuery(Context context, String userSearchText) {
        FilterSettings settings = getFilterSettings(context);

        StringBuilder query = new StringBuilder();

        String beginMonth = settings.getBeginMonth().length() == 1 ? "0" + settings.getBeginMonth() : settings.getBeginMonth();
        String beginDay = settings.getBeginDay().length() == 1 ? "0" + settings.getBeginDay() : settings.getBeginDay();
        String beginDate = settings.getBeginYear() + beginMonth + beginDay;

        query.append("begin_date=").append(beginDate)
                .append("&sort=").append(settings.getSortSelectedText());

        if (userSearchText != "")
                query.append("&fq=\"").append(userSearchText).append("\"");

        query.append("&fq=source:(\"The New York Times\")");

        StringBuilder news_desk = new StringBuilder();

        if (settings.isCheckedArts())
            news_desk.append("\"Arts\" ");

        if (settings.isCheckedFashion())
            news_desk.append("\"Fashion & Style\" ");

        if (settings.isCheckedSports())
            news_desk.append("\"Sports\" ");

        if (news_desk.toString() != "")
            query.append("&fq=news_desk(").append(news_desk.toString().trim()).append(")");

        return query.toString();
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
