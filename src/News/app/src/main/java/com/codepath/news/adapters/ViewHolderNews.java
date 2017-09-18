package com.codepath.news.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.news.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rdeshpan on 9/18/2017.
 */

public class ViewHolderNews extends RecyclerView.ViewHolder {
    @BindView(R.id.tvHeadline)
    TextView tvHeadline;

    @BindView(R.id.tvSnippet)
    TextView tvSnippet;

    @BindView(R.id.tvSectionName)
    TextView tvSectionName;

    @BindView(R.id.ivThumbnail)
    ImageView ivThumbnail;


    public ViewHolderNews(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
