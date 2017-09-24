package com.codepath.news.utils;

import android.databinding.BindingAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.codepath.news.R;

/**
 * Created by rdeshpan on 9/22/2017.
 */

public class CustomBindingAdapter {

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {

        Glide.with(view.getContext())
                .load(CommonHelper.getImageUrl(imageUrl))
                .override(600, 600)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(view);
    }

    @BindingAdapter({"bind:year", "bind:month", "bind:day"})
    public static void updateDate(DatePicker dpDate, int year, int month, int day) {
        dpDate.updateDate(year, month, day);
    }

    @BindingAdapter({"bind:selection"})
    public static void setSelectedItem(Spinner spinner, int position) {
        spinner.setSelection(position);
    }
}
