package com.example.sellfoodmini.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sellfoodmini.Controller.ChatbotActivity;
import com.example.sellfoodmini.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private TextView Title;
    private Button btnBack, btnSetting;
    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fabChatbot;
    private boolean isNavigating = false; // Cờ kiểm tra để tránh vòng lặp
    private float dX, dY; // Biến lưu vị trí ban đầu khi kéo
    private int screenWidth, screenHeight; // Kích thước màn hình

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Title = findViewById(R.id.title);
        btnBack = findViewById(R.id.btnBack);
        btnSetting = findViewById(R.id.btnSetting);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        fabChatbot = findViewById(R.id.fab_chatbot);

        // Lấy kích thước màn hình
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        // Xử lý kéo thả cho FAB
        fabChatbot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Lưu vị trí ban đầu của ngón tay
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        return true; // Tiêu thụ sự kiện để tiếp tục xử lý kéo

                    case MotionEvent.ACTION_MOVE:
                        // Cập nhật vị trí của FAB khi kéo
                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;

                        // Giới hạn FAB trong màn hình (dừng ngay tại cạnh)
                        newX = Math.max(0, Math.min(newX, screenWidth - view.getWidth()));
                        newY = Math.max(0, Math.min(newY, screenHeight - view.getHeight() - getStatusBarHeight()));

                        view.setX(newX);
                        view.setY(newY);
                        return true;

                    case MotionEvent.ACTION_UP:
                        // Khi thả tay, di chuyển FAB đến cạnh gần nhất và dừng lại
                        snapToNearestEdge(view);
                        // Kiểm tra xem có phải là một cú nhấp chuột không
                        if (Math.abs(event.getRawX() + dX - view.getX()) < 5 &&
                                Math.abs(event.getRawY() + dY - view.getY()) < 5) {
                            startActivity(new Intent(MainActivity.this, ChatbotActivity.class));
                        }
                        return true;
                }
                return false;
            }
        });

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
        }
    }

    private void updateBackButtonState() {
        boolean canGoBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
        btnBack.setEnabled(canGoBack);
        btnBack.setAlpha(btnBack.isEnabled() ? 1f : 0.5f);
    }

    // Hàm di chuyển FAB đến cạnh gần nhất và dừng lại
    private void snapToNearestEdge(View view) {
        float x = view.getX();
        float y = view.getY();
        int fabWidth = view.getWidth();
        int fabHeight = view.getHeight();

        // Tính khoảng cách đến các cạnh
        float leftDistance = x;
        float rightDistance = screenWidth - (x + fabWidth);
        float topDistance = y;
        float bottomDistance = screenHeight - (y + fabHeight) - getStatusBarHeight();

        // Xác định cạnh gần nhất theo trục X
        float targetX;
        if (leftDistance < rightDistance) {
            targetX = 0; // Dính vào cạnh trái
        } else {
            targetX = screenWidth - fabWidth; // Dính vào cạnh phải
        }

        // Xác định cạnh gần nhất theo trục Y
        float targetY;
        if (topDistance < bottomDistance) {
            targetY = 0; // Dính vào cạnh trên
        } else {
            targetY = screenHeight - fabHeight - getStatusBarHeight(); // Dính vào cạnh dưới
        }

        // Đặt vị trí ngay lập tức (không dùng animation)
        view.setX(targetX);
        view.setY(targetY);
    }

    // Lấy chiều cao của status bar
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}