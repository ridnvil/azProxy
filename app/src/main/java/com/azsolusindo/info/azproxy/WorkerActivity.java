package com.azsolusindo.info.azproxy;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WorkerActivity extends AppCompatActivity {
    private static int SPALSH_TIME_OUT = 1000;
    static String host = "139.162.44.129";
    static int port = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ProxySettings.setProxy(getApplicationContext(),host,port);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(WorkerActivity.this, WebActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPALSH_TIME_OUT);
    }
}
