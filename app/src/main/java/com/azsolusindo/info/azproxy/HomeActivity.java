package com.azsolusindo.info.azproxy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.azsolusindo.info.azproxy.model.publicIP;
import com.azsolusindo.info.azproxy.remote.IPService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    static String host1 = "139.162.44.129";
    static int port1 = 80;
    static String host2 = "";
    static int port2 = 8080;
    IPService mService;
    MaterialCardView cardTrafic;
    FloatingActionButton btnCon, btnConnected, btnDisconnected;
    Animation fabClose,fabOpen,rotateForward,rotateBackward;
    CheckBox checkDevice, showIP, showTraffic, useProxy;
    TextView ipPublic, infoIP;
    boolean isOpen = false;
    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;
    boolean proxyActiv = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getMobileTxBytes();

        mService = Common.getIPService();
        btnCon = findViewById(R.id.btnConnect);
        btnConnected = findViewById(R.id.btnConnected);
        btnDisconnected = findViewById(R.id.btnDisconnected);
        checkDevice = findViewById(R.id.checkDevice);
        showIP = findViewById(R.id.checkShowIP);
        showTraffic = findViewById(R.id.checkShowTraffic);
        ipPublic = findViewById(R.id.txtViewIpPublic);
        infoIP = findViewById(R.id.tvInfoIP);
        cardTrafic = findViewById(R.id.traficCard);
        useProxy = findViewById(R.id.checkUseProxy);

        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        showIP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (showIP.isChecked()){
                    String IP = ipPublic.getText().toString().trim();
                    getIP(IP);

                    ipPublic.setVisibility(View.VISIBLE);
                    infoIP.setVisibility(View.VISIBLE);
                }else{
                    ipPublic.setVisibility(View.GONE);
                    infoIP.setVisibility(View.GONE);
                }
            }
        });

        showTraffic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (showTraffic.isChecked()){
                    cardTrafic.setVisibility(View.VISIBLE);
                }else{
                    cardTrafic.setVisibility(View.INVISIBLE);
                }
            }
        });


        if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Warning!");
            alert.setMessage("Do not support device");
            alert.show();
        }else {
            mHandler.postDelayed(mRunnable, 1000);
        }

        cardTrafic.setVisibility(View.INVISIBLE);

        cardTrafic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkerViewActivity();
            }
        });

        btnCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (connectionNet()){
                    animateFab();
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
        Log.w("Coba On Create", String.valueOf(savedInstanceState));
    }

    @Override
    protected void onResume() {
        ProxySettings.setProxy(getApplicationContext(),host1,port1);
        super.onResume();
        Log.w("Coba On Resume", "onResume()");
        //System.setProperty("http.ProxyHosts", "");

        useProxy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (useProxy.isChecked()){
                    DisebleProxy(host2,port2);
                }else{
                    //ProxyActiv(false);
                }
            }
        });
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        super.onContextMenuClosed(menu);
        finish();
    }

    private boolean ProxyActiv(boolean prx){
        if(prx){
            return ProxySettings.setProxy(getApplicationContext(),host1,port1);
        }
        return false;
    }

    private void EnableProxy(String host, int port){
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", port + "");

        System.setProperty("https.proxyHost", host);
        System.setProperty("https.proxyPort", port + "");
    }

    private void DisebleProxy(String host, int port){
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", port + "");

        System.setProperty("https.proxyHost", host);
        System.setProperty("https.proxyPort", port + "");
    }

    private final Runnable mRunnable = new Runnable() {
        @SuppressLint("SetTextI18n")
        public void run() {
            TextView RX = findViewById(R.id.RX);
            TextView TX = findViewById(R.id.TX);
            long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
            RX.setText(Long.toString(rxBytes)+" Bytes");
            if (rxBytes>1024){
                long rxKByte = rxBytes/1024;
                RX.setText(Long.toString(rxKByte)+ " KB");
                if (rxKByte>1024){
                    long rxMByte = rxKByte/1024;
                    RX.setText(Long.toString(rxMByte)+" MB");
                    if (rxMByte>1024){
                        long rxGByte = rxMByte/1024;
                        RX.setText(Long.toString(rxGByte)+" GB");
                    }
                }
            }

            long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;
            TX.setText(Long.toString(txBytes)+" Bytes");
            if (txBytes>1024){
                long txKByte = txBytes/1024;
                TX.setText(Long.toString(txKByte)+" KB");
                if (txKByte>1024){
                    long txMByte = txKByte/1024;
                    TX.setText(Long.toString(txMByte)+" MB");
                    if (txMByte>1024){
                        long txGByte = txMByte/1024;
                        TX.setText(Long.toString(txGByte)+" GB");
                    }
                }
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    private void getIP(String query){
        mService.getPublicIP(query).enqueue(new Callback<publicIP>() {
            @Override
            public void onResponse(@NonNull Call<publicIP> call, @NonNull Response<publicIP> response) {
                assert response.body() != null;
                if (response.body() == null){
                    ipPublic.setText("Error IP");
                }else{
                    ipPublic.setText(response.body().getQuery());
                }
            }

            @Override
            public void onFailure(@NonNull Call<publicIP> call, Throwable t) {

            }
        });
    }

    public boolean connectionNet(){
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm.getDefaultProxy();
        }
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void animateFab(){
        if (isOpen){
            btnConnected.startAnimation(fabClose);
            btnDisconnected.startAnimation(fabClose);
            btnConnected.setClickable(false);
            btnDisconnected.setClickable(false);
            isOpen=false;
        }else {
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
