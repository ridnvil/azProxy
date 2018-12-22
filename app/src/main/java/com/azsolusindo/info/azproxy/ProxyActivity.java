package com.azsolusindo.info.azproxy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

public class ProxyActivity extends AppCompatActivity {
    CheckBox checkProxy;
    ImageButton btnProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);

        checkProxy = findViewById(R.id.checkProxy);
        btnProxy = findViewById(R.id.btnProxy);

        btnProxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkProxy.isChecked()){
                    Intent homeIntent = new Intent(ProxyActivity.this, WorkerActivity.class);
                    startActivity(homeIntent);
                }else{
                    Intent homeIntent = new Intent(ProxyActivity.this, WorkerNoActivity.class);
                    startActivity(homeIntent);
                }
            }
        });
    }
}
