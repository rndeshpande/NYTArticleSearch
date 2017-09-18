package com.codepath.news.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.news.R;
import com.codepath.news.activities.DetailsActivity;
import com.codepath.news.models.News;
import com.codepath.news.utils.ConfigHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by rdeshpan on 9/17/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<News> mNewsItems;
    Context mContext;

    public NewsAdapter(Context context, ArrayList<News> newsItems) {
        mContext = context;
        mNewsItems= newsItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final RecyclerView.ViewHolder viewHolder;
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.list_news, parent, false);
        viewHolder = new ViewHolderNews(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        int position = viewHolder.getAdapterPosition();
                        News newsItem = mNewsItems.get(position);
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("url", newsItem.webUrl);
                        context.startActivity(intent);
                    }
            });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolderNews viewHolderNews = (ViewHolderNews) viewHolder;

        News newsItem = mNewsItems.get(position);

        final ImageView ivThumbnail = viewHolderNews.ivThumbnail;
        final TextView tvHeadline = viewHolderNews.tvHeadline;
        final TextView tvSnippet = viewHolderNews.tvSnippet;
        final TextView tvSectionName = viewHolderNews.tvSectionName;

        tvHeadline.setText(newsItem.headline.main);

        String imagePath = ConfigHelper.getImageUrl(newsItem.multimedia.get(0).url);

        Picasso.with(viewHolderNews.ivThumbnail.getContext())
                .load(imagePath)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivThumbnail);

    }



    @Override
    public int getItemCount() {
        return mNewsItems.size();
    }
}
