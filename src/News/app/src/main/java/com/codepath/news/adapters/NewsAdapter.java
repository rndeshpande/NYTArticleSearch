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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.news.R;
import com.codepath.news.activities.DetailsActivity;
import com.codepath.news.models.News;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by rdeshpan on 9/17/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<News> mNewsItems;
    private Context mContext;

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
        itemView.setOnClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            News newsItem = mNewsItems.get(position);
            displayWebpage(newsItem.webUrl);
        });

        return viewHolder;
    }

    private void displayWebpage(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_action_name);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);

        int requestCode = 100;
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setActionButton(bitmap, mContext.getString(R.string.share_link),pendingIntent, true);
        builder.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();

        customTabsIntent.launchUrl(mContext, Uri.parse(url));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolderNews viewHolderNews = (ViewHolderNews) viewHolder;
        News newsItem = mNewsItems.get(position);
        viewHolderNews.bind(mContext, newsItem);
    }

    @Override
    public int getItemCount() {
        return mNewsItems.size();
    }
}
