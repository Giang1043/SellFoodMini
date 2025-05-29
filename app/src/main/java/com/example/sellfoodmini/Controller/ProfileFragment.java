package com.example.sellfoodmini.Controller;

import static com.example.sellfoodmini.Controller.LoginActivity.showLoginConfirmation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sellfoodmini.Business.User.Customer;
import com.example.sellfoodmini.Database.AppDatabase;
import com.example.sellfoodmini.Database.UserDAO;
import com.example.sellfoodmini.R;

public class ProfileFragment extends Fragment {
    private AppDatabase db;
    private Customer customer;

    public ProfileFragment(){};

    private SharedPreferences prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(requireContext());
        UserDAO userDAO = db.userDAO();

        // Get SharedPreferences
        prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

        int customerId = prefs.getInt("customerId", -1);

        if (customerId == -1) {
            Toast.makeText(requireContext(), "Please log in to use this service", Toast.LENGTH_SHORT).show();
            showLoginConfirmation(requireContext());
            return;
        }
        customer = userDAO.selectCustomerById(customerId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (customer == null) {return null;}

        ImageView profileImage = view.findViewById(R.id.profileImage);
        TextView profileName = view.findViewById(R.id.profileName);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        LinearLayout accountDetailsLayout = view.findViewById(R.id.accountDetailsLayout);
        LinearLayout wishlistLayout = view.findViewById(R.id.wishlistLayout);
        LinearLayout orderHistoryLayout = view.findViewById(R.id.orderHistoryLayout);
        LinearLayout contactUsLayout = view.findViewById(R.id.contactUsLayout);

        accountDetailsLayout.setOnClickListener(this::onAccountDetailsClicked);
        wishlistLayout.setOnClickListener(this::onWishlistClicked);
        orderHistoryLayout.setOnClickListener(this::onOrderHistoryClicked);
        contactUsLayout.setOnClickListener(this::onContactUsClicked);

        // Hiển thị thông tin người dùng
        profileName.setText(customer.getName());
        profileImage.setImageResource(R.drawable.shiny_blue_diamod);

        btnLogout.setOnClickListener(v-> showLogoutConfirmation());
        return view;
    }
    // hiển thị phần xác nhận log out
    private void showLogoutConfirmation() {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Xác Nhận Đăng Xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Có", (d, which) -> {
                    // User clicked "Yes"
                    performLogout();
                })
                .setNegativeButton("Hủy", (d, which) -> {
                    //- do nothing, hủy yêu cầu
                })
                .setCancelable(true) // Allow dismissal by tapping outside
                .show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.black));
    }

    private void performLogout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("customerId");
        editor.remove("isLoggedIn");
        editor.apply();

        Toast.makeText(requireContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();

        // Delay finish to show toast
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            requireActivity().finish();
        }, 300); // 500ms delay to ensure toast is visible
    }
    public void onAccountDetailsClicked(View view) {
        AccountDetailFragment accountDetailFragment = new AccountDetailFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, accountDetailFragment);
        transaction.addToBackStack(null); // Allow back navigation
        transaction.commit();
    }

    public void onWishlistClicked(View view) {
        WishlistFragment wishlistFragment = new WishlistFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, wishlistFragment);
        transaction.addToBackStack(null); // Allow back navigation
        transaction.commit();
    }

    public void onOrderHistoryClicked(View view) {
        OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, orderHistoryFragment);
        transaction.addToBackStack(null); // Allow back navigation
        transaction.commit();
    }
    public void onContactUsClicked(View view) {
        // TODO: Implement navigation to Contact Us fragment/activity
        Toast.makeText(requireContext(), "Contact Us clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
