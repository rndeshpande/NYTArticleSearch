package com.codepath.news.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.news.databinding.ListNewsBinding;
import com.codepath.news.handlers.NewsHandler;
import com.codepath.news.models.News;

/**
 * Created by rdeshpan on 9/18/2017.
 */

public class ViewHolderNews extends RecyclerView.ViewHolder {

    final ListNewsBinding binding;

    public TextView tvHeadline;

    public TextView tvSnippet;

    public TextView tvSectionName;

    public ImageView ivThumbnail;

    public Button btnShare;

    public ViewHolderNews(View itemView) {
        super(itemView);
        binding = ListNewsBinding.bind(itemView);
        binding.setHandler(new NewsHandler());
    }

    public void bind(News news) {
        binding.setNews(news);
        binding.executePendingBindings();
    }
}
