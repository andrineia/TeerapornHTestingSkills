package com.example.andrineia.iscb;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

public class LaunchingActivity extends Activity {
    Handler handler;
    Runnable runnable;
    long delay_time;
    long time = 2000L;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_layout);
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                Intent intent  = new Intent(LaunchingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        };
    }


    public void onResume() {
        super.onResume();
        delay_time = time;
        handler.postDelayed(runnable, delay_time);
        time = System.currentTimeMillis();
    }

    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }
}
