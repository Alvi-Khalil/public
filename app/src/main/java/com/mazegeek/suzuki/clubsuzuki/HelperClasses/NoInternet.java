package com.mazegeek.suzuki.clubsuzuki.HelperClasses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mazegeek.suzuki.clubsuzuki.R;

public class NoInternet extends AppCompatActivity {

    Button buttonRetry;
    public ConnectionDetector connectionDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        buttonRetry=findViewById(R.id.retry_btn);
        connectionDetector = new ConnectionDetector(this);

        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectionDetector.isConnected()) {

                }
                else {
                    finish();
                }
            }
        });
    }
}
