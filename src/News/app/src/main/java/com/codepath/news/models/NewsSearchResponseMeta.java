package com.codepath.news.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by rdeshpan on 9/19/2017.
 */

public class NewsSearchResponseMeta {
    @SerializedName("hits")
    public int hits;

    @SerializedName("offset")
    public int offset;
}
