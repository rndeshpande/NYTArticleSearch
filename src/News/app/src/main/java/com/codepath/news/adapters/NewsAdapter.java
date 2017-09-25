package com.codepath.news.adapters;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.news.R;
import com.codepath.news.models.News;

import java.util.ArrayList;

/**
 * Created by rdeshpan on 9/17/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<News> mNewsItems;
    private Context mContext;

    private final static int MULTIMEDIA = 1;
    private final static int TEXT_ONLY = 2;

    public NewsAdapter(Context context, ArrayList<News> newsItems) {
        mContext = context;
        mNewsItems = newsItems;
    }

    @Override
    public int getItemViewType(int position) {
        News news = mNewsItems.get(position);

        if (news.multimedia.size() > 0) {
            return MULTIMEDIA;
        } else {
            return  TEXT_ONLY;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final RecyclerView.ViewHolder viewHolder;
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;

        switch (viewType) {
            case MULTIMEDIA:
                itemView = inflater.inflate(R.layout.list_news, parent, false);
                viewHolder = new ViewHolderNews(itemView);
                break;
            case TEXT_ONLY:
                itemView = inflater.inflate(R.layout.list_news_textonly, parent, false);
                viewHolder = new ViewHolderNewsTextOnly(itemView);
                break;
            default:
                viewHolder = null;
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        News news = mNewsItems.get(position);

        switch (viewHolder.getItemViewType()) {
            case MULTIMEDIA:
                ViewHolderNews viewHolderNews = (ViewHolderNews) viewHolder;
                viewHolderNews.bind(news);
                break;
            case TEXT_ONLY:
                ViewHolderNewsTextOnly viewHolderNewsTextOnly = (ViewHolderNewsTextOnly) viewHolder;
                viewHolderNewsTextOnly.bind(news);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mNewsItems.size();
    }
}
