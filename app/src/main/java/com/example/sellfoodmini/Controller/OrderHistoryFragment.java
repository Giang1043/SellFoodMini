package com.example.sellfoodmini.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellfoodmini.Business.Order.Order;
import com.example.sellfoodmini.Business.Order.OrderAdapter;
import com.example.sellfoodmini.Database.AppDatabase;
import com.example.sellfoodmini.Database.OrderDAO;
import com.example.sellfoodmini.R;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryFragment extends Fragment {

    private RecyclerView recyclerOrderHistory;
    private OrderAdapter orderAdapter;
    private List<Order> orderHistory;
    private AppDatabase db;
    private OrderDAO orderDAO;
    private int customerId;

    public OrderHistoryFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(requireContext());
        orderDAO = db.orderDAO();

        SharedPreferences prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        customerId = prefs.getInt("customerId", -1);

        // Fetch orders directly from OrderDAO for the logged-in customer
        orderHistory = customerId != -1 ? orderDAO.selectSetByCustomer(customerId) : new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        recyclerOrderHistory = view.findViewById(R.id.recyclerOrderHistory);

        // Setup RecyclerView
        recyclerOrderHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(orderHistory, requireActivity());
        recyclerOrderHistory.setAdapter(orderAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (customerId != -1) {
            // Refresh order list from database
            orderHistory = orderDAO.selectSetByCustomer(customerId);
            orderAdapter.updateOrderList(orderHistory);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}