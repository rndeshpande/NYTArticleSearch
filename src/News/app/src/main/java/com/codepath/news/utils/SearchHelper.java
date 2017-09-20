package com.codepath.news.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by rdeshpan on 9/18/2017.
 */

public class SearchHelper {

    public static String getFilterParams(Context context) {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sort = sharedpreferences.getString("sort", "oldest");
        String newsDesk = sharedpreferences.getString("news_desk", "");
        String beginDate = sharedpreferences.getString("begin_date", "18510101");

        String searchParams = "begin_date=" + beginDate + "&sort=" + sort + "&fq=source:(\"The New York Times\")";
        if (newsDesk != "")
            searchParams += "&fq=news_desk(" + newsDesk + ")" ;

        return  searchParams;
    }
}
