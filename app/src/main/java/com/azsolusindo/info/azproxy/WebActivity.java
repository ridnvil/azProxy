package com.azsolusindo.info.azproxy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
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
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.subisakah.hideqlib.ApiResponse;
import com.subisakah.hideqlib.DeviceInformation;
import com.subisakah.hideqlib.InfoKey;
import com.subisakah.hideqlib.ServerLog;
import com.subisakah.hideqlib.WebViewProxy;

import org.jetbrains.annotations.NotNull;

import java.net.Proxy;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

import static android.app.PendingIntent.getActivity;
import static android.net.Proxy.PROXY_CHANGE_ACTION;

public class WebActivity extends AppCompatActivity{
    static String host1 = "139.162.44.129";
    static int port1 = 80;

    WebView webView;
    String url;
    LinearLayout layoutWebView;
    EditText txtUrl;
    ImageButton btnGo, btnStop;
    ProgressBar progressBar;
    RelativeLayout webViewNoConnection;
    FrameLayout frameLayout;
    String LoIP, OS, Brow, DeviceName;
    Map<String, String> params = new HashMap<>();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        ProxySettings.setProxy(getApplicationContext(),host1,port1);
//        EnableProxy(host1,port1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        layoutWebView = findViewById(R.id.layoutWebView);
        frameLayout =  findViewById(R.id.frameLayout);
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.webView);
        webViewNoConnection = findViewById(R.id.webViewNoConnection);
        txtUrl = findViewById(R.id.editUrl);
        btnGo = findViewById(R.id.goBtn);
        btnStop = findViewById(R.id.stopBtn);
        btnGo.setImageResource(R.drawable.ic_search_black_24dp);
        btnStop.setImageResource(R.drawable.ic_close_black_24dp);

        registerForContextMenu(webView);

        frameLayout.setVisibility(View.GONE);
        webViewNoConnection.setVisibility(View.GONE);
        btnStop.setVisibility(View.GONE);

        progressBar.setMax(100);


        //postDataToServer();
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableProxy(host1,port1);
                //postDataToServer();
                url = txtUrl.getText().toString();

                webViewProxy(buildUrl(url));
//                CeckConnectivity(buildUrl(url));
                v.setFocusable(true);

                Log.w("Coba", Settings.System.NETWORK_PREFERENCE);

                Log.w("URL",buildUrl(url));
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.stopLoading();
            }
        });
    }

    private void EnableProxy(String host, int port){
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", port + "");

        System.setProperty("https.proxyHost", host);
        System.setProperty("https.proxyPort", port + "");
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.webmenu,menu);
    }

    public String buildUrl(String uri){
        if (uri.startsWith("http://")  || uri.startsWith("https://"))
            return uri;
        return "http://".concat(uri);
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

    private void onForwardPressed(){

        if (webView.canGoForward()){
            webView.goForward();
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

    @SuppressLint("SetJavaScriptEnabled")
    public void webViewProxy(String webAdress){
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);

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
                txtUrl.setText(url);
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
        webView.loadUrl(webAdress);

    }


    public void postDataToServer(){

        DeviceName = DeviceInformation.map().get(InfoKey.DEVICE_NAME);
        LoIP = DeviceInformation.map().get(InfoKey.PRIVATE_IP4);
        OS = DeviceInformation.map().get(InfoKey.OS_VERSION);
        Brow = DeviceInformation.map().get(InfoKey.MAC_ADDRESS);

        params.put("submit", "Login");
        params.put("user","android");
        params.put("pass","android");

        if (Brow != null){
            params.put("brow", Brow);
        }

        if (LoIP != null){
            params.put("LoIP", LoIP);
        }

        if (OS != null){
            params.put("OS", OS+" on "+DeviceName);
        }

        ServerLog.post(getApplicationContext(),
                "http://azsolusindo.info/userProcess2.php",
                (HashMap<String, String>) params, new ApiResponse() {
            @Override
            public void onSuccess(@NotNull Object response) {
                Log.w("Send Data", response.toString());
            }

            @Override
            public void onError(@NotNull Exception e) {
                Log.w("Send Data","Error send Data");
            }
        });
    }

    private class MyWebView extends WebChromeClient{
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
