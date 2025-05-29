package com.example.sellfoodmini.Controller;

import static com.example.sellfoodmini.Controller.LoginActivity.showLoginConfirmation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sellfoodmini.Business.User.Customer;
import com.example.sellfoodmini.Database.AppDatabase;
import com.example.sellfoodmini.Database.UserDAO;
import com.example.sellfoodmini.R;

public class AccountDetailFragment extends Fragment {
    private TextView username, email, phone, address;
    private Customer customer;
    private AppDatabase db;
    private SharedPreferences prefs;

    public AccountDetailFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(requireContext());
        UserDAO userDAO = db.userDAO();

        // Get SharedPreferences
        prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

        int customerId = prefs.getInt("customerId", -1);

        if (customerId == -1) {
            Toast.makeText(requireContext(), "Please log in to view this section", Toast.LENGTH_SHORT).show();
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
        View view = inflater.inflate(R.layout.fragment_account_detail, container, false);

        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phoneNumber);
        address = view.findViewById(R.id.address);

        username.setText(customer.getName());
        email.setText(customer.getEmail());
        phone.setText(customer.getPhone());
        address.setText(customer.getAddress());

        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
