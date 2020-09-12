package com.shalaka.womansafegurd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView i1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        i1=findViewById(R.id.i1);
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        i1.startAnimation(animation);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                 startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                 finish();
            }
        },5000);
    }
}