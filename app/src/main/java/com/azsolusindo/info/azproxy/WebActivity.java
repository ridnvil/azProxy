package com.azsolusindo.info.azproxy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.toolbox.HttpClientStack;
import com.subisakah.hideqlib.ApiResponse;
import com.subisakah.hideqlib.DeviceInformation;
import com.subisakah.hideqlib.InfoKey;
import com.subisakah.hideqlib.ServerLog;
import com.subisakah.hideqlib.WebViewProxy;

import org.jetbrains.annotations.NotNull;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

import static android.app.PendingIntent.getActivity;

public class WebActivity extends AppCompatActivity{
    WebView webView;
    static String host = "139.162.44.129";
    static int port = 80;
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
        enableProxy();
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

        CeckConnectivity();

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                client.proxy();
                Log.w("Coba", String.valueOf(client));

                //postDataToServer();
                url = txtUrl.getText().toString();
                v.setFocusable(true);
                webViewProxy(buildUrl(url));
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

    private void CeckConnectivity(){
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            webView.loadUrl("https://www.google.com/");
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
        }else{

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

    public void enableProxy(){
//        System.setProperty("http.proxyHost", host);
//        System.setProperty("http.proxyPort", port+"");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                //WebViewProxy.setEnabled(getApplicationContext(), host,port);
                System.setProperty("http.proxyHost", host);
                System.setProperty("http.proxyPort", port+"");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void disableProxy(){
        System.setProperty("http.proxyHost", "");
        System.setProperty("http.proxyPort", "");
    }

    public void webViewProxy(String webAdress){

        webView.getSettings().setJavaScriptEnabled(true);
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

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String uri) {
                //return super.shouldOverrideUrlLoading(view, uri);
                view.loadUrl(uri);
                return true;
            }
        });

        webView.setWebChromeClient(new MyWebView(){
            @Override
            public void onProgressChanged(WebView view, int progress){
                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);
                btnGo.getDrawable();

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
