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

    ImageButton btnGo, btnBack,btnForward;
    Fragment fragment;
    String host = "139.162.44.129";
    int port = 80;
    String address;
    static String link;

    FrameLayout frameLayout;
    public static EditText urlTextTesting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_layout);

        btnGo = findViewById(R.id.btnGoo);
        btnBack = findViewById(R.id.backBtn);
        btnForward = findViewById(R.id.forwardBtn);

        urlTextTesting = findViewById(R.id.txtUrlTesting);


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = urlTextTesting.getText().toString();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

}
