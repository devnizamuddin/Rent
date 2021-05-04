package com.example.rent.screen.home;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rent.pojo.MyLocation;
import com.example.rent.R;


public class ViewLocationActivity extends AppCompatActivity {

    WebView web_view;
    String baseUrl = "https://www.google.com/maps/search/?api=1&query=";
    String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);

        web_view = findViewById(R.id.web_view);

        WebSettings webSettings = web_view.getSettings();
        web_view.getSettings().setJavaScriptEnabled(true);
        //web_view.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        web_view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        web_view.getSettings().setAppCacheEnabled(true);
        web_view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        // webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        //webSettings.setEnableSmoothTransition(true);

        MyLocation myLocation = (MyLocation) getIntent().getSerializableExtra("myLocation");
        String lat =myLocation.getLat();
        String lon =myLocation.getLon();
        webUrl = baseUrl+lat+","+lon;

        web_view.setWebViewClient(new WebViewClient());
        web_view.loadUrl(webUrl);


    }
}
