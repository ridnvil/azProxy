package com.azsolusindo.info.azproxy;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WorkerNoActivity extends AppCompatActivity {
    private static int SPALSH_TIME_OUT = 1000;
    static String host = "";
    static int port = 433;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ProxySettings.setProxy(getApplicationContext(),host,port);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_no);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(WorkerNoActivity.this, WebActivity.class);
                startActivity(homeIntent);
                //finish();
            }
        },SPALSH_TIME_OUT);
    }
}
