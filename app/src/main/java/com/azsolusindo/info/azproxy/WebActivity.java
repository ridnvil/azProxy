package com.azsolusindo.info.azproxy;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Build;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.subisakah.hideqlib.ApiResponse;
import com.subisakah.hideqlib.DeviceInformation;
import com.subisakah.hideqlib.InfoKey;
import com.subisakah.hideqlib.ServerLog;
import com.subisakah.hideqlib.WebViewProxy;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.SSLServerSocket;

public class WebActivity extends AppCompatActivity{
    WebView webView;
    static String host = "139.162.44.129";
    static int port = 80;
    String url;
    LinearLayout layoutWebView;
    EditText txtUrl;
    ImageButton btnGo;
    ImageView logoUrl;
    ProgressBar progressBar;
    FrameLayout frameLayout;
    String LoIP, OS, Brow, DeviceName;
    Map<String, String> params = new HashMap<>();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web);

        layoutWebView = findViewById(R.id.layoutWebView);
        frameLayout =  findViewById(R.id.frameLayout);
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.webView);
        txtUrl = findViewById(R.id.editUrl);
        btnGo = findViewById(R.id.goBtn);

        frameLayout.setVisibility(View.GONE);

        progressBar.setMax(100);

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //postDataToServer();
                url = txtUrl.getText().toString();

                if (url.equals("http://qq28800.com")){
                    enableProxy();
                    webViewWithProxy(url);
                }else{
                    disableProxy();
                    webViewNoProxy(url);
                }

                Log.w("URL",url);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.webmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.forward:
                onForwardPressed();
                break;
            case R.id.refresh:
                webView.reload();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onForwardPressed(){

        if (webView.canGoForward()){
            webView.goForward();
        }else{
            Toast.makeText(this, "Can't go Forward!", Toast.LENGTH_SHORT).show();
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
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", port+"");
    }

    public void disableProxy(){
        System.setProperty("http.proxyHost", "");
        System.setProperty("http.proxyPort", "");
    }

    public void webViewWithProxy(String webAdress){

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setProgress(0);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                txtUrl.setText(url);
            }

        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int progress){
                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);
            }

//            public void onGeolocationPermissionsPromp(String origin, GeolocationPermissions.Callback callback){
//                callback.invoke(origin, true, false);
//            }
        });
        webView.setVerticalScrollBarEnabled(false);
        webView.loadUrl(webAdress);

    }

    public void webViewNoProxy(String webAdress){

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);


        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                txtUrl.setText(url);
            }

        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int progress){
                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);
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

            }
        });
    }
}
