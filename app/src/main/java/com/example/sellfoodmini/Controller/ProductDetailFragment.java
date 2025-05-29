package com.example.sellfoodmini.Controller;

import static com.example.sellfoodmini.Controller.LoginActivity.showLoginConfirmation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sellfoodmini.Business.Cart.Cart;
import com.example.sellfoodmini.Business.Cart.Cart_Item;
import com.example.sellfoodmini.Business.Food.Food;
import com.example.sellfoodmini.Business.User.Customer;
import com.example.sellfoodmini.Database.AppDatabase;
import com.example.sellfoodmini.Database.CartDAO;
import com.example.sellfoodmini.Database.CustomerDAO;
import com.example.sellfoodmini.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDetailFragment extends Fragment {

    private Food food;
    private Cart cart;
    private Customer customer;
    private AppDatabase db;
    private CartDAO cartDAO;
    private CustomerDAO customerDAO;
    private CheckBox btnAddToList;

    public ProductDetailFragment(){};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(requireContext());
        cartDAO = db.cartDAO();
        customerDAO = db.customerDAO();

        if (getArguments() != null) {
            food = (Food) getArguments().getSerializable("food");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        // Hiển thị thông tin món ăn
        ImageView productImage = view.findViewById(R.id.productImage);
        TextView productName = view.findViewById(R.id.productName);
        TextView productPrice = view.findViewById(R.id.productPrice);
        TextView productDescription = view.findViewById(R.id.productDescription);
        Button btnAddToCart = view.findViewById(R.id.btnAddToCart);
        btnAddToList = view.findViewById(R.id.btnAddToList);

        SharedPreferences prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        int customerId = prefs.getInt("customerId", -1);

        if (customerId != -1) {
            customer = customerDAO.selectById(customerId);
        }

        if (food != null) {
            productImage.setImageResource(food.getImageResId());
            productName.setText(food.getName());
            // format Vd: 5000đ --> 5.000đ
            Locale vietnam = new Locale("vi", "VN");
            NumberFormat formatter = NumberFormat.getNumberInstance(vietnam);
            String formattedPrice = formatter.format(food.getPrice()) + "đ";
            productPrice.setText(formattedPrice);

            String descriptionText = "<b>Mô tả: </b>" + food.getDescription();
            productDescription.setText(Html.fromHtml(descriptionText, Html.FROM_HTML_MODE_LEGACY));

            // Check if food is in wishlist and set initial state
            if (customer != null && customer.getWishlist() != null) {
                boolean isInWishlist = customer.getWishlist().stream()
                        .anyMatch(wishlistFood -> wishlistFood.getId() == food.getId());
                btnAddToList.setChecked(isInWishlist);
            } else {
                btnAddToList.setChecked(false);
            }

        } else {
            Toast.makeText(requireContext(), "Lỗi: Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
        }
        // add to cart button listener
        btnAddToCart.setOnClickListener(v -> {
            if (customerId == -1) {
                Toast.makeText(requireContext(), "Vui lòng đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                showLoginConfirmation(requireContext());
                return;
            }
            // tạo cart cho user nếu chưa có
            cart = cartDAO.selectByCustomerId(customerId);
            if (cart == null) {
                cart = new Cart(customerId, new ArrayList<>());
                cartDAO.insert(cart);
            }
            addToCart();
            navigateToCartFragment();
            Toast.makeText(requireContext(), "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });

        // Add to wishlist button listener
        btnAddToList.setOnClickListener(v -> {
            if (customerId == -1) {
                Toast.makeText(requireContext(), "Vui lòng đăng nhập để thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                showLoginConfirmation(requireContext());
                btnAddToList.setChecked(false);
                return;
            }

            if (customer != null && food != null) {
                if (btnAddToList.isChecked()) {
                    customer.addToWishlist(food);
                    Toast.makeText(requireContext(), "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                } else {
                    customer.removeFromWishlist(food);
                    Toast.makeText(requireContext(), "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                }
                customerDAO.update(customer);
            }
        });

        return view;
    }
    private void addToCart() {
        List<Cart_Item> items = cart.getItems();
        boolean found = false;
        for (Cart_Item item : items) {
            if (item.getFoodId() == food.getId()) {
                item.setQuantity(item.getQuantity() + 1); // Increment quantity
                found = true;
                break;
            }
        }
        if (!found) {
            items.add(new Cart_Item(cart.getCartId(), food.getId(), 1)); // Add new item with quantity 1
        }
        cartDAO.update(cart);
    }
    private void navigateToCartFragment() {
        CartFragment cartFragment = new CartFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, cartFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
