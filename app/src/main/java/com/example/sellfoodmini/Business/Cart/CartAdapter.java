package com.example.sellfoodmini.Business.Cart;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellfoodmini.Business.Food.Food;
import com.example.sellfoodmini.Controller.ProductDetailFragment;
import com.example.sellfoodmini.Database.CartDAO;
import com.example.sellfoodmini.Database.FoodDAO;
import com.example.sellfoodmini.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<Cart_Item> cartItems;
    private final OnCartUpdatedListener cartUpdatedListener;
    private final FoodDAO foodDAO;
    private final CartDAO cartDAO;
    private final Cart cart;
    private final FragmentActivity activity; // Added to handle fragment transactions

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        Button btnDelete;
        TextView foodName, foodPrice;
        EditText foodQuantity;
        TextWatcher quantityWatcher;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.cart_item_image);
            foodName = itemView.findViewById(R.id.cart_item_name);
            foodPrice = itemView.findViewById(R.id.cart_item_price);
            foodQuantity = itemView.findViewById(R.id.cart_item_quantity);
            btnDelete = itemView.findViewById(R.id.cart_item_delete);
        }
    }

    public CartAdapter(Cart cart, CartDAO cartDAO, List<Cart_Item> cartItems, FoodDAO foodDAO,
                       FragmentActivity activity, OnCartUpdatedListener listener) {
        this.cart = cart;
        this.cartDAO = cartDAO;
        this.cartItems = cartItems;
        this.foodDAO = foodDAO;
        this.activity = activity; // Initialize FragmentActivity
        this.cartUpdatedListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart_Item item = cartItems.get(position);
        Food food = foodDAO.selectById(item.getFoodId());

        holder.foodImage.setImageResource(food.getImageResId());
        holder.foodName.setText(food.getName());

        Locale vietnam = new Locale("vi", "VN");
        NumberFormat formatter = NumberFormat.getNumberInstance(vietnam);
        String formattedPrice = formatter.format(food.getPrice()) + "đ";
        holder.foodPrice.setText(formattedPrice);

        holder.foodQuantity.setText(String.valueOf(item.getQuantity()));

        // Remove previous listeners to avoid duplicates
        if (holder.quantityWatcher != null) {
            holder.foodQuantity.removeTextChangedListener(holder.quantityWatcher);
        }

        // Handle quantity update when focus is lost or Enter is pressed
        holder.foodQuantity.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                updateQuantity(holder, position);
            }
        });

        holder.foodQuantity.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                updateQuantity(holder, position);
                holder.foodQuantity.clearFocus();
                return true;
            }
            return false;
        });

        // TextWatcher to capture changes (but not act until focus lost or Enter)
        holder.quantityWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing here; wait for focus loss or Enter
            }
        };
        holder.foodQuantity.addTextChangedListener(holder.quantityWatcher);

        // Handle remove button click
        holder.btnDelete.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                holder.itemView.postDelayed(() -> {
                    cartItems.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    notifyItemRangeChanged(adapterPosition, cartItems.size());
                    updateCartInDatabase();
                }, 100);
            }
        });

        // Click listener for item to go to ProductDetailFragment
        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Food selectedFood = foodDAO.selectById(cartItems.get(adapterPosition).getFoodId());
                ProductDetailFragment detailFragment = new ProductDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("food", selectedFood);
                detailFragment.setArguments(bundle);

                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    // Helper method to update quantity
    private void updateQuantity(CartViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition == RecyclerView.NO_POSITION) return;

        String input = holder.foodQuantity.getText().toString();
        try {
            int newQuantity = input.isEmpty() ? 0 : Integer.parseInt(input);
            if (newQuantity <= 0) {
                holder.itemView.postDelayed(() -> {
                    if (adapterPosition < cartItems.size()) {
                        cartItems.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                        notifyItemRangeChanged(adapterPosition, cartItems.size());
                    }
                    updateCartInDatabase();
                }, 100);
            } else {
                cartItems.get(adapterPosition).setQuantity(newQuantity);
                notifyItemChanged(adapterPosition);
                updateCartInDatabase();
            }
        } catch (NumberFormatException e) {
            holder.foodQuantity.setText(String.valueOf(cartItems.get(adapterPosition).getQuantity())); // Revert
        }
    }

    private void updateCartInDatabase() {
        if (cart != null && cartDAO != null) {
            cart.setItems(cartItems);
            cartDAO.update(cart);
        }
        if (cartUpdatedListener != null) {
            cartUpdatedListener.calculateTotalPrice();
        }
    }

    public void clearCartView() {
        cartItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    public interface OnCartUpdatedListener {
        void calculateTotalPrice();
    }
}