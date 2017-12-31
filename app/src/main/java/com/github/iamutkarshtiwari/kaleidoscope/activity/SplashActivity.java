package com.github.iamutkarshtiwari.kaleidoscope.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.github.iamutkarshtiwari.kaleidoscope.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            /**
             * This method will be executed once the timer is over
             */
            @Override
            public void run() {
                // Sends to home activity
                sendToHomeActivity();
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public void sendToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
