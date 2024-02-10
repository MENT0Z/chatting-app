package com.mruraza.mychat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class splashscreen extends AppCompatActivity {
Animation fromtop,fromsecond;
CardView crdvw;
TextView appname,developername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        crdvw = findViewById(R.id.id_of_cardview);
        appname = findViewById(R.id.app_name_id_to_show);
        developername = findViewById(R.id.developerid);

        fromtop = AnimationUtils.loadAnimation(this,R.anim.from_top_animation);
        fromsecond = AnimationUtils.loadAnimation(this,R.anim.from_bottom_animation);
        crdvw.setAnimation(fromsecond);
        appname.setAnimation(fromtop);
        developername.setAnimation(fromtop);




        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashscreen.this,MainActivity.class);
   startActivity(intent);
   finish();
            }
        },5000);


    }
}