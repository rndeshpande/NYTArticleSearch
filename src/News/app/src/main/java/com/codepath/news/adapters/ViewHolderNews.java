package com.codepath.news.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.news.R;
import com.codepath.news.models.News;
import com.codepath.news.utils.CommonHelper;

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

    public void bind(Context context, News newsItem) {
        tvHeadline.setText(newsItem.headline.main);
        tvSectionName.setText(newsItem.sectionName);
        tvSnippet.setText(newsItem.snippet);

        if (newsItem.multimedia.size() > 0) {
            String imagePath =  CommonHelper.getImageUrl(newsItem.multimedia.get(0).url);

            Glide.with(context)
                    .load(imagePath)
                    .override(300,300)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(ivThumbnail);
        }

    }
}
