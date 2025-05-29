package com.example.sellfoodmini.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sellfoodmini.Data.InitActivity;
import com.example.sellfoodmini.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.splash_logo);
        logo.setImageResource(R.drawable.shop_logo);

        // Thêm hiệu ứng fade-in cho logo
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);

        // Use Handler with Looper to avoid deprecated constructor
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, InitActivity.class); // Navigate to InitActivity
            startActivity(intent);
            finish();
        }, 2000); // Hiển thị splash screen trong 2 giây
    }
}