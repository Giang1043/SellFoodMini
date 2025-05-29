package com.example.sellfoodmini.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sellfoodmini.Controller.ChatbotActivity;
import com.example.sellfoodmini.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView Title;
    private Button btnBack, btnSetting;
    private BottomNavigationView bottomNavigation;
    private boolean isNavigating = false; // Cờ kiểm tra để tránh vòng lặp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Title = findViewById(R.id.title);
        btnBack = findViewById(R.id.btnBack);
        btnSetting = findViewById(R.id.btnSetting); // Nếu đã thêm btnSetting
        bottomNavigation = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), false);
            setTitleForItem(R.id.nav_home);
        }

        btnBack.setOnClickListener(v -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }
        });

        btnSetting.setOnClickListener(v -> {
            // TODO: Chuyển đến màn hình cài đặt (chưa cài đặt)
        });

        getSupportFragmentManager().addOnBackStackChangedListener(this::updateUI);

        bottomNavigation.setOnItemSelectedListener(item -> {
            if (isNavigating) return false;

            if (item.getItemId() == R.id.nav_chatbot) {
                startActivity(new Intent(MainActivity.this, ChatbotActivity.class));
                return true;
            }

            Fragment fragment = item.getItemId() == R.id.nav_home ? new HomeFragment() :
                    item.getItemId() == R.id.nav_order ? new CartFragment() :
                            new ProfileFragment();

            loadFragment(fragment, true);
            setTitleForItem(item.getItemId());
            return true;
        });
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        isNavigating = true;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment);
        if (addToBackStack) transaction.addToBackStack(null);
        transaction.commit();
        updateBackButtonState();
        bottomNavigation.postDelayed(() -> isNavigating = false, 200);
    }

    private void updateUI() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment == null) return;
        int navId = 0;
        if (fragment instanceof HomeFragment) {
            navId = R.id.nav_home;
        } else if (fragment instanceof CartFragment) {
            navId = R.id.nav_order;
        } else if (fragment instanceof ProfileFragment) {
            navId = R.id.nav_profile;
        }
        setTitleForItem(navId);
        bottomNavigation.setSelectedItemId(navId);
        updateBackButtonState();
    }

    private void setTitleForItem(int itemId) {
        if (itemId == R.id.nav_order) {
            Title.setText("Đặt Hàng");
        } else if (itemId == R.id.nav_profile) {
            Title.setText("Thông Tin Cá Nhân");
        } else if (itemId == R.id.nav_home) {
            Title.setText("Trang Chủ");
        } else if (itemId == R.id.nav_chatbot) {
            Title.setText("Chatbot"); // Đặt tiêu đề khi chọn Chatbot
        }
    }

    private void updateBackButtonState() {
        boolean canGoBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
        btnBack.setEnabled(canGoBack);
        btnBack.setAlpha(btnBack.isEnabled() ? 1f : 0.5f);
    }
}