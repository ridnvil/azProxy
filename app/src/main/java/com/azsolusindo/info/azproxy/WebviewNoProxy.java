package com.azsolusindo.info.azproxy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Properties;

import static com.azsolusindo.info.azproxy.WebActivity.port;


public class WebviewNoProxy extends Fragment {
    //String host = null;
    //int port = 80;
    WebView webView;
    static String URL;

    public WebviewNoProxy() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //setProxy();

        Properties proper = System.getProperties();
        proper.remove("http.proxyHost");
        proper.remove("http.proxyPort");
        proper.remove("https.proxyHost");
        proper.remove("https.proxyPort");

        Log.i("Coba", String.valueOf(proper.remove("http.proxyHost")));

        View v = inflater.inflate(R.layout.fragment_webview_no_proxy, container, false);
        webView = v.findViewById(R.id.webViewNoproxy);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(URL);

        return v;
    }

    public void setProxy(){
        //System.setProperty("http.proxyHost", host);
        //System.setProperty("http.proxyPort", port+"");

        //System.setProperty("https.proxyHost", host);
        //System.setProperty("https.proxyPort", port + "");
    }
}
