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

import com.example.sellfoodmini.Business.Food.Food;
import com.example.sellfoodmini.Business.User.WishlistAdapter;
import com.example.sellfoodmini.Business.User.Customer;
import com.example.sellfoodmini.Database.AppDatabase;
import com.example.sellfoodmini.Database.CustomerDAO;
import com.example.sellfoodmini.R;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment implements WishlistAdapter.OnWishlistItemClickListener {

    private RecyclerView recyclerWishlist;
    private WishlistAdapter wishlistAdapter;
    private Customer customer;
    private List<Food> wishList;
    private CustomerDAO customerDAO;
    private AppDatabase db;

    public WishlistFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(requireContext());
        customerDAO = db.customerDAO();

        SharedPreferences prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        int customerId = prefs.getInt("customerId", -1);

        customer = customerDAO.selectById(customerId);
        wishList = customer != null ? customer.getWishlist() : new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        recyclerWishlist = view.findViewById(R.id.recyclerWishlist);

        // Setup RecyclerView
        recyclerWishlist.setLayoutManager(new LinearLayoutManager(getContext()));
        wishlistAdapter = new WishlistAdapter(wishList, requireActivity(), this);
        recyclerWishlist.setAdapter(wishlistAdapter);

        return view;
    }

    @Override
    public void onDeleteClick(Food food) {
        if (customer != null) {
            customer.removeFromWishlist(food);
            customerDAO.update(customer);
            wishlistAdapter.updateWishlist(customer.getWishlist());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}