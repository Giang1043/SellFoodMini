package com.example.sellfoodmini.Controller;

import static com.example.sellfoodmini.Controller.LoginActivity.showLoginConfirmation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellfoodmini.Business.Cart.Cart;
import com.example.sellfoodmini.Business.Cart.CartAdapter;
import com.example.sellfoodmini.Business.Cart.Cart_Item;
import com.example.sellfoodmini.Business.Cart.CheckoutAdapter;
import com.example.sellfoodmini.Business.Order.Order;
import com.example.sellfoodmini.Business.Order.OrderStatus;
import com.example.sellfoodmini.Business.Order.Order_Item;
import com.example.sellfoodmini.Business.User.Customer;
import com.example.sellfoodmini.Database.AppDatabase;
import com.example.sellfoodmini.Database.CartDAO;
import com.example.sellfoodmini.Database.CustomerDAO;
import com.example.sellfoodmini.Database.FoodDAO;
import com.example.sellfoodmini.Database.OrderDAO;
import com.example.sellfoodmini.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CartFragment extends Fragment implements CartAdapter.OnCartUpdatedListener {
    private TextView txtTotalPrice;
    private CartAdapter cartAdapter;
    private AppDatabase db;
    private CustomerDAO customerDAO;
    private CartDAO cartDAO;
    private OrderDAO orderDAO;
    private FoodDAO foodDAO;
    private Cart cart;
    private Customer customer;
    private double totalCartCost = 0;
    private int customerId;

    public CartFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(requireContext());
        cartDAO = db.cartDAO();
        orderDAO = db.orderDAO();
        foodDAO = db.foodDAO();
        customerDAO = db.customerDAO();

        SharedPreferences prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        customerId = prefs.getInt("customerId", -1);
        if (customerId == -1) {
            Toast.makeText(requireContext(), "Vui lòng đăng nhập để sử dụng dịch vụ", Toast.LENGTH_SHORT).show();
            showLoginConfirmation(requireContext());
            return;
        }

        customer = customerDAO.selectById(customerId);
        cart = cartDAO.selectByCustomerId(customerId);
        if (cart == null) {
            cart = new Cart(customerId, new ArrayList<>());
            cartDAO.insert(cart);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        if (cart == null) {
            return null;
        }

        RecyclerView recyclerCart = view.findViewById(R.id.recycler_cart);
        txtTotalPrice = view.findViewById(R.id.txt_total_price);
        Button btnCheckout = view.findViewById(R.id.btn_checkout);

        cartAdapter = new CartAdapter(cart, cartDAO, cart.getItems(), foodDAO, requireActivity(), this);
        recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCart.setAdapter(cartAdapter);

        btnCheckout.setOnClickListener(v -> {
            if (cart.getItems().isEmpty()) {
                Toast.makeText(getContext(), "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            } else {
                showCheckoutDialog();
            }
        });

        return view;
    }

    private void showCheckoutDialog() {
        Dialog dialog = new Dialog(requireContext(), android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        Objects.requireNonNull(dialog.getWindow()).setWindowAnimations(R.style.DialogAnimation);
        dialog.setContentView(R.layout.dialog_checkout);

        // Initialize views
        Button btnClose = dialog.findViewById(R.id.btn_close);
        RecyclerView recyclerCheckoutItems = dialog.findViewById(R.id.recycler_checkout_items);
        TextInputEditText etPhone = dialog.findViewById(R.id.et_phone);
        TextInputEditText etAddress = dialog.findViewById(R.id.et_address);
        MaterialButton btnSelectAddress = dialog.findViewById(R.id.btn_select_address);
        RadioGroup rgPaymentMethod = dialog.findViewById(R.id.rg_payment_method);
        TextView tvTotalPrice = dialog.findViewById(R.id.tv_total_price);
        MaterialButton btnPlaceOrder = dialog.findViewById(R.id.btn_place_order);

        // Set up RecyclerView for checkout items
        CheckoutAdapter checkoutAdapter = new CheckoutAdapter(cart.getItems(), foodDAO);
        recyclerCheckoutItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerCheckoutItems.setAdapter(checkoutAdapter);

        // Load phone number from Customer
        if (customer != null && customer.getPhone() != null && !customer.getPhone().isEmpty()) {
            etPhone.setText(customer.getPhone());
        } else {
            etPhone.setText("");
        }

        // Load address from Customer (if available)
        if (customer != null && customer.getAddress() != null && !customer.getAddress().isEmpty()) {
            etAddress.setText(customer.getAddress());
        } else {
            etAddress.setText("");
        }

        // Set total price
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat formatter = NumberFormat.getNumberInstance(vietnam);
        String formattedPrice = formatter.format(totalCartCost) + "đ";
        tvTotalPrice.setText(formattedPrice);

        // Handle close button
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // Handle select address button
        btnSelectAddress.setOnClickListener(v -> {
            String query = etAddress.getText().toString().trim();
            if (query.isEmpty()) {
                query = "address";
            }
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(query));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        // Handle place order button
        btnPlaceOrder.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            int selectedPaymentId = rgPaymentMethod.getCheckedRadioButtonId();

            if (phone.isEmpty()) {
                etPhone.setError("Vui lòng nhập số điện thoại");
                return;
            }

            if (address.isEmpty()) {
                etAddress.setError("Vui lòng nhập địa chỉ giao hàng");
                return;
            }

            if (selectedPaymentId == -1) {
                Toast.makeText(getContext(), "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            String paymentMethod = selectedPaymentId == R.id.rb_momo ? "Momo" : "ZaloPay";

            // Update phone in Customer if changed
            if (customer != null && (customer.getPhone() == null || !customer.getPhone().equals(phone))) {
                customer.setPhone(phone);
                customerDAO.update(customer);
            }

            if (checkout()) {
                Toast.makeText(getContext(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                cartAdapter.clearCartView();
                calculateTotalPrice();
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Đặt hàng thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    public void calculateTotalPrice() {
        double totalPrice = cart.getTotalCartCost(db.foodDAO());
        totalCartCost = totalPrice;
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat formatter = NumberFormat.getNumberInstance(vietnam);
        String formattedPrice = formatter.format(totalPrice) + "đ";
        txtTotalPrice.setText(formattedPrice);
    }

    public boolean checkout() {
        try {
            Order newOrder = new Order(cart.getCustomerId(), new ArrayList<>(), totalCartCost, OrderStatus.CHO_XU_LY);
            orderDAO.insert(newOrder);

            int newId = orderDAO.getNewestOrderId(customerId);
            newOrder.setOrderId(newId);

            List<Order_Item> orderItems = newOrder.getItems();
            for (Cart_Item item : cart.getItems()) {
                orderItems.add(new Order_Item(newId, item.getFoodId(), item.getQuantity()));
            }
            newOrder.setItems(orderItems);
            orderDAO.update(newOrder);

            cart.getItems().clear();
            cartDAO.update(cart);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cart != null) {
            calculateTotalPrice();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}