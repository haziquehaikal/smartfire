package com.picotech.smartfire.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;

import com.picotech.smartfire.R;

public class Activity_Streaming extends AppCompatActivity {

    private Button camera, video;
    public ListView listView;
    //Define URL
    private static final String URL = "http://159.89.201.201/";
    //Define WebView

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.activity_streaming);
        getSupportActionBar().setTitle("Live Streaming");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView)findViewById(R.id.webView); //get webView
        webView.setWebViewClient(new WebViewClient()); //set webView client
        //WebView.setDesktopMode(true);
        WebSettings webSettings = webView.getSettings();// initiate webView settings
        webSettings.setJavaScriptEnabled(true); //allow webView perform javascript
        webSettings.setBuiltInZoomControls(true); //show zoom control

        webView.loadUrl(URL);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return false;
    }
}