package com.codepath.news.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rdeshpan on 9/18/2017.
 */

public class NewsHeadline {
    @SerializedName("main")
    public String main;

    @SerializedName("print_headline")
    public String printHeadline;
}
