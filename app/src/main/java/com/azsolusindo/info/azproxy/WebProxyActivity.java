package com.azsolusindo.info.azproxy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class WebProxyActivity extends AppCompatActivity {
    EditText textURL;
    ImageButton btnGo,btnStop;
    FrameLayout frameLayout;
    ProgressBar progressBar;
    WebView webView;
    RelativeLayout webNoConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_proxy);

        textURL = findViewById(R.id.editUrl2);
        frameLayout = findViewById(R.id.frameLayout2);
        progressBar = findViewById(R.id.progressBar2);
        webView = findViewById(R.id.webView2);
        webNoConnection = findViewById(R.id.webViewNoConnection);
        btnGo = findViewById(R.id.goBtn2);
        btnStop = findViewById(R.id.stopBtn2);
        btnGo.setImageResource(R.drawable.ic_search_black_24dp);
        btnStop.setImageResource(R.drawable.ic_close_black_24dp);

        frameLayout.setVisibility(View.GONE);
        webNoConnection.setVisibility(View.GONE);
        btnStop.setVisibility(View.GONE);

        progressBar.setMax(100);
        registerForContextMenu(webView);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = textURL.getText().toString();

                CeckConnectivity(buildUrl(url));
                v.setFocusable(true);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.stopLoading();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.webmenu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:
                webView.reload();
            case R.id.forward:
                onForwardPressed();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public String buildUrl(String uri){
        if (uri.startsWith("http://")  || uri.startsWith("https://"))
            return uri;
        return "http://".concat(uri);
    }

    private void CeckConnectivity(String urladd){
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            webViewProxy(urladd);
        } else {
            webView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

        if(webView.canGoBack()){
            webView.goBack();
        }else{
            finish();
        }
    }

    private void onForwardPressed(){

        if (webView.canGoForward()){
            webView.goForward();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void webViewProxy(String uri){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.setLongClickable(true);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.w("Coba", "Ini Long Click");
                return false;
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                btnStop.setVisibility(View.VISIBLE);
                btnGo.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                frameLayout.setVisibility(View.GONE);
                textURL.setText(url);
                btnGo.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.GONE);
            }
        });

        webView.setWebChromeClient(new MyWebView(){
            @Override
            public void onProgressChanged(WebView view, int progress){
                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);
                btnGo.getDrawable();

            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                super.onPermissionRequest(request);
                Log.w("Coba", String.valueOf(request));
            }

            public void onGeolocationPermissionsPromp(String origin, GeolocationPermissions.Callback callback){
                callback.invoke(origin, true, false);
            }
        });

        webView.setVerticalScrollBarEnabled(false);
        webView.loadUrl(uri);
    }

    private class MyWebView extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyWebView(){}

        public Bitmap getDefaultVideoPoster(){
            if (mCustomView == null){
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView(){
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback){
            if(this.mCustomView != null){
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1,-1));
            getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }
}
