package com.azsolusindo.info.azproxy;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.subisakah.hideqlib.WebViewProxy;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ImageView img;
    TextView txtV;
    Animation frombottom;
    Animation fromtop;
    private static int SPALSH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView)findViewById(R.id.imgLog);
        txtV = (TextView)findViewById(R.id.textView);

        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        txtV.setAnimation(frombottom);
        img.setAnimation(fromtop);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, ProxyActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPALSH_TIME_OUT);
    }
}
