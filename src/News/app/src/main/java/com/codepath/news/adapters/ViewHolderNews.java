package com.codepath.news.adapters;

import android.databinding.BindingAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.news.databinding.ListNewsBinding;

/**
 * Created by rdeshpan on 9/18/2017.
 */

public class ViewHolderNews extends RecyclerView.ViewHolder {

    final ListNewsBinding binding;

    TextView tvHeadline;

    TextView tvSnippet;

    TextView tvSectionName;

    ImageView ivThumbnail;


    public ViewHolderNews(View itemView) {
        super(itemView);
        binding = ListNewsBinding.bind(itemView);
    }


}
