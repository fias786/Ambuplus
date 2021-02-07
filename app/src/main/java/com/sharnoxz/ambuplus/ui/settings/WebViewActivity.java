package com.sharnoxz.ambuplus.ui.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sharnoxz.ambuplus.R;


public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = findViewById(R.id.WebView);
        webView.getSettings().setJavaScriptEnabled(true);
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }
}
