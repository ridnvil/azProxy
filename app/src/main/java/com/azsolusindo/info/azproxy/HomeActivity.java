package com.azsolusindo.info.azproxy;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.subisakah.hideqlib.WebViewProxy;

public class HomeActivity extends AppCompatActivity {
    FloatingActionButton btnCon, btnConnected, btnDisconnected;
    Animation fabClose,fabOpen,rotateForward,rotateBackward;
    CheckBox checkDevice;
    boolean isChecked = false;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnCon = (FloatingActionButton)findViewById(R.id.btnConnect);
        btnConnected = (FloatingActionButton)findViewById(R.id.btnConnected);
        btnDisconnected = (FloatingActionButton)findViewById(R.id.btnDisconnected);
        checkDevice = (CheckBox)findViewById(R.id.checkDevice);

        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        btnCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });

        btnConnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                Toast.makeText(HomeActivity.this,"Successfully Connect to Proxy", Toast.LENGTH_SHORT).show();
                btnCon.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.connectBtn)));

                if (checkDevice.isChecked()){
                    WorkerViewActivity();
                }else{
                    WebViewActivity();
                    //Log.w(this.getClass().getName() , String.valueOf(WebViewProxy.Companion.isUsingProxy(getApplicationContext())));
                }
            }
        });


        btnDisconnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                Toast.makeText(HomeActivity.this,"Disconnected Proxy", Toast.LENGTH_SHORT).show();
                btnCon.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.disconnectBtn)));

            }
        });
    }

    private void animateFab(){
        if (isOpen){
            btnCon.startAnimation(rotateForward);
            btnConnected.startAnimation(fabClose);
            btnDisconnected.startAnimation(fabClose);
            btnConnected.setClickable(false);
            btnDisconnected.setClickable(false);
            isOpen=false;
        }else {
            btnCon.startAnimation(rotateForward);
            btnConnected.startAnimation(fabOpen);
            btnDisconnected.startAnimation(fabOpen);
            btnConnected.setClickable(true);
            btnDisconnected.setClickable(true);
            isOpen=true;
        }
    }

    private void WorkerViewActivity(){
        Intent intent = new Intent(HomeActivity.this, TestingLayout.class);
        startActivity(intent);
    }

    private void WebViewActivity(){
        Intent homeIntent = new Intent(HomeActivity.this, WebActivity.class);
        startActivity(homeIntent);
    }
}
