package com.azsolusindo.info.azproxy;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.LocalServerSocket;
import android.net.NetworkInfo;
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
import android.widget.TextView;
import android.widget.Toast;

import com.azsolusindo.info.azproxy.model.publicIP;
import com.azsolusindo.info.azproxy.remote.IPService;
import com.subisakah.hideqlib.WebViewProxy;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    IPService mService;
    FloatingActionButton btnCon, btnConnected, btnDisconnected;
    Animation fabClose,fabOpen,rotateForward,rotateBackward;
    CheckBox checkDevice;
    TextView ipPublic;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mService = Common.getIPService();
        btnCon = findViewById(R.id.btnConnect);
        btnConnected = findViewById(R.id.btnConnected);
        btnDisconnected = findViewById(R.id.btnDisconnected);
        checkDevice = findViewById(R.id.checkDevice);
        ipPublic = findViewById(R.id.txtViewIpPublic);

        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        btnCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (koneksi()==true){
                    animateFab();
                    String IP = ipPublic.getText().toString().trim();
                    getIP(IP);
                }else{
                    Toast.makeText(HomeActivity.this, "No Connection Internet", Toast.LENGTH_SHORT).show();
                }
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

    private void getIP(String query){
        mService.getPublicIP(query).enqueue(new Callback<publicIP>() {
            @Override
            public void onResponse(Call<publicIP> call, Response<publicIP> response) {
                ipPublic.setText(response.body().getQuery());
            }

            @Override
            public void onFailure(Call<publicIP> call, Throwable t) {

            }
        });
    }

    public boolean koneksi(){
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
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
        Intent intent = new Intent(HomeActivity.this, WorkerActivity.class);
        startActivity(intent);
    }

    private void WebViewActivity(){
        Intent homeIntent = new Intent(HomeActivity.this, WebActivity.class);
        startActivity(homeIntent);
    }
}
