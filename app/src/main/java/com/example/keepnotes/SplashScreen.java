package com.example.keepnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    LottieAnimationView lottie;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        lottie=findViewById(R.id.lottie_splash);

        new Handler().postDelayed(new Runnable() {

// Using handler with postDelayed called runnable run method

            @Override

            public void run() {


                FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser==null){
                    startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                }
                else {
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                }
                finish();

            }

        }, 3000); // wait for 3 seconds

    }
}