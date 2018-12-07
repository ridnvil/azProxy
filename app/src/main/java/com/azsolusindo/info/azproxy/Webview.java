package com.azsolusindo.info.azproxy;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.subisakah.hideqlib.WebViewProxy;

import static com.azsolusindo.info.azproxy.WebActivity.host;
import static com.azsolusindo.info.azproxy.WebActivity.port;

public class Webview extends Fragment {
    WebView webView;
    static String URL;
    String link;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setProxy("139.162.44.129", 80);

        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_webview, container, false);
        // Inflate the layout for this fragment

        webView = v.findViewById(R.id.webViewFragment);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                URL.equals(url);
                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(URL);

        return v;
    }

    public void setProxy(String hostP, int portP){
        System.setProperty("http.proxyHost", hostP);
        System.setProperty("http.proxyPort", portP+"");

        System.setProperty("https.proxyHost", hostP);
        System.setProperty("https.proxyPort", portP+"");
    }

}
