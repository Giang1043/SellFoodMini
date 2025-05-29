package com.example.sellfoodmini.Data;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class InitActivity extends AppCompatActivity {
    private static final String TAG = "InitActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseInitializer.initializeDatabase(this);
        Log.d(TAG, "InitActivity completed");
        finish(); // Close the activity after running
    }
}