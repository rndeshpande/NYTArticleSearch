package com.codepath.news.handlers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.codepath.news.R;

/**
 * Created by rdeshpan on 9/23/2017.
 */

public class NewsHandler {
    public void onShare(View view, String url) {
        Context context = view.getContext();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share_intent_title)));
    }

    public void loadUrl(View v, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        Context context = v.getContext();

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_name);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);

        int requestCode = 100;
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setActionButton(bitmap, context.getString(R.string.share_link), pendingIntent, true);
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();

        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
