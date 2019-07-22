package com.bufferchime.agriculturefact;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;

import wail.splacher.com.splasher.lib.SplasherActivity;
import wail.splacher.com.splasher.models.SplasherConfig;
import wail.splacher.com.splasher.utils.Const;

public class Launch extends SplasherActivity {




    @Override
    public void initSplasher(SplasherConfig config) {
        config.setReveal_start(Const.START_TOP_LEFT)
                //---------------
                .setAnimationDuration(5000)
                //---------------
                .setLogo(R.drawable.squarelogo)
                .setLogo_animation(Techniques.BounceIn)
                .setAnimationLogoDuration(2000)
                .setLogoWidth(500)
                //---------------
                .setTitle("Agriculture Facts")
                .setTitleColor(Color.parseColor("#ffffff"))
                .setTitleAnimation(Techniques.Bounce)
                .setTitleSize(24)
                //---------------
                .setSubtitle("All your facts at fingertips")
                .setSubtitleColor(Color.parseColor("#ffffff"))
                .setSubtitleAnimation(Techniques.FadeIn)
                .setSubtitleSize(16);
        //---------------


    }

    @Override
    public void onSplasherFinished() {
        Toast.makeText(this, "Lets Begin", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Home.class));

    }



}