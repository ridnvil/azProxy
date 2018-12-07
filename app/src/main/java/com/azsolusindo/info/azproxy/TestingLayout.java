package com.azsolusindo.info.azproxy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.Properties;

public class TestingLayout extends AppCompatActivity {
    ImageButton btnGo;
    Fragment fragment;
    String host = "139.162.44.129";
    int port = 80;
    String address;
    static String link;

    FrameLayout frameLayout;
    EditText urlTextTesting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_layout);

        btnGo = findViewById(R.id.btnGoo);
        urlTextTesting = findViewById(R.id.txtUrlTesting);


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = urlTextTesting.getText().toString();

                if(address.equals("http://qq10100.com")){
                    Webview.URL = address;
                    fragmentWithProxy();
                    Log.w("PROXY", "With Proxy");
                }else{
                    WebviewNoProxy.URL = address;
                    fragmentNoProxy();
                    Log.w("PROXY", "NO Proxy");
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void fragmentWithProxy(){
        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        fragment = new Webview();
        fragTrans.replace(R.id.relatifFragment, fragment);
        fragTrans.commit();
    }

    public void fragmentNoProxy(){
        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        fragment = new WebviewNoProxy();
        fragTrans.replace(R.id.relatifFragment, fragment);
        fragTrans.commit();
    }

}
