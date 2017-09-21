package com.codepath.news.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.news.R;
import com.codepath.news.models.News;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.wvDetails)
    WebView wvDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String webUrl = intent.getStringExtra("web_url");
        wvDetails.setWebViewClient(new WebViewClient());
        wvDetails.loadUrl(webUrl);
    }
}
