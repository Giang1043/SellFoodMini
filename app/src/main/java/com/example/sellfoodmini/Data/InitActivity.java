package com.example.sellfoodmini.Data;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sellfoodmini.Controller.MainActivity;

public class InitActivity extends AppCompatActivity {
    private static final String TAG = "InitActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseInitializer.initializeDatabase(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Log.d(TAG, "InitActivity completed");
        finish(); // Close the activity after running
    }
}