package com.codepath.news.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rdeshpan on 9/18/2017.
 */

public class NewsMultimedia {
    @SerializedName("type")
    public String type;

    @SerializedName("subtype")
    public String subType;

    @SerializedName("url")
    public String url;
}
