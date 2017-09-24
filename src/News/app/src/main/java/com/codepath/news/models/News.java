package com.codepath.news.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    public Date pubDate;

    public String dateFormatted;

    @SerializedName("section_name")
    public String sectionName;

    @SerializedName("new_desk")
    public String newDesk;

    @SerializedName("multimedia")
    public ArrayList<NewsMultimedia> multimedia;

    public String getDateFormatted(){
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        String dateString = formatter.format(this.pubDate);
        return dateString;
    }
}
