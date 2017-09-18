package com.codepath.news.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by rdeshpan on 9/18/2017.
 */

public class NewsSearchResponse {
    @SerializedName("status")
    public String id;

    @SerializedName("response")
    public NewsSearchDocs newsDocs;
}
