package com.codepath.news.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by rdeshpan on 9/18/2017.
 */

public class NewsSearchDocs {

    @SerializedName("docs")
    public ArrayList<News> newsItems;
}
