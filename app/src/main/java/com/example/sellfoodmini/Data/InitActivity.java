package com.example.sellfoodmini.Data;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sellfoodmini.Controller.MainActivity;
import com.example.sellfoodmini.Database.AppDatabase;

public class InitActivity extends AppCompatActivity {
    private static final String TAG = "InitActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabase db = AppDatabase.getInstance(this);
        // db.clearAllTables(); // Xóa toàn bộ dữ liệu cũ (tùy chọn)
        DatabaseInitializer.initializeDatabase(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Log.d(TAG, "InitActivity completed");
        finish();
    }
}