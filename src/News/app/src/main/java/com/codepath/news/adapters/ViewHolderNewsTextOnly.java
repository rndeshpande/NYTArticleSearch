package com.codepath.news.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.news.databinding.ListNewsTextonlyBinding;
import com.codepath.news.handlers.NewsHandler;
import com.codepath.news.models.News;

/**
 * Created by rdeshpan on 9/24/2017.
 */

public class ViewHolderNewsTextOnly extends RecyclerView.ViewHolder {

    final ListNewsTextonlyBinding binding;

    public TextView tvHeadline;

    public TextView tvSnippet;

    public TextView tvSectionName;

    public ImageView ivThumbnail;

    public Button btnShare;

    public ViewHolderNewsTextOnly(View itemView) {
        super(itemView);
        binding = ListNewsTextonlyBinding.bind(itemView);
        binding.setHandler(new NewsHandler());
    }

    public void bind(News news) {
        binding.setNews(news);
        binding.executePendingBindings();
    }
}
