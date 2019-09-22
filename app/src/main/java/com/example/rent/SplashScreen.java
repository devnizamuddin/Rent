package com.example.rent;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rent.Auth.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    ImageView app_logo_img;
    TextView app_name_txt, app_goal_txt;
    Animation blink_anim, bounce_anim, fadein_anim, fadeout_anim,
            lefttoright_anim, mixed_anim, righttoleft_anim, rotate_anim
            ,sample_anim,zoomin_anim,zoomout_anim;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        app_logo_img = findViewById(R.id.app_logo_img);
        app_name_txt = findViewById(R.id.app_name_txt);
        app_goal_txt = findViewById(R.id.app_goal_txt);

        blink_anim = AnimationUtils.loadAnimation(this, R.anim.blink_anim);
        bounce_anim = AnimationUtils.loadAnimation(this, R.anim.bounce_anim);
        fadein_anim = AnimationUtils.loadAnimation(this, R.anim.fadein_anim);
        fadeout_anim = AnimationUtils.loadAnimation(this, R.anim.fadeout_anim);
        lefttoright_anim = AnimationUtils.loadAnimation(this, R.anim.lefttoright_anim);
        mixed_anim = AnimationUtils.loadAnimation(this, R.anim.mixed_anim);
        righttoleft_anim = AnimationUtils.loadAnimation(this, R.anim.righttoleft_anim);
        rotate_anim = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        sample_anim = AnimationUtils.loadAnimation(this, R.anim.sample_anim);
        zoomin_anim = AnimationUtils.loadAnimation(this, R.anim.zoomin_anim);
        zoomout_anim = AnimationUtils.loadAnimation(this, R.anim.zoomout_anim);

        app_logo_img.setAnimation(fadein_anim);
        app_name_txt.setAnimation(lefttoright_anim);
        app_goal_txt.setAnimation(righttoleft_anim);


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               startActivity(new Intent(SplashScreen.this, LoginActivity.class));
               finish();

            }
        },3000);

    }


}
