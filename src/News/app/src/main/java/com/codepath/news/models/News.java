package com.codepath.news.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by rdeshpan on 9/18/2017.
 */

public class News {
    @SerializedName("_id")
    public String id;

    @SerializedName("web_url")
    public String webUrl;

    @SerializedName("snippet")
    public String snippet;

    @SerializedName("source")
    public String source;

    @SerializedName("headline")
    public NewsHeadline headline;

    @SerializedName("pub_date")
    public String publishedDate;

    @SerializedName("section_name")
    public String sectionName;

    @SerializedName("new_desk")
    public String newDesk;

    @SerializedName("multimedia")
    public ArrayList<NewsMultimedia> multimedia;
}
