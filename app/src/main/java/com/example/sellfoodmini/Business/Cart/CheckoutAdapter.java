package com.example.sellfoodmini.Business.Cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellfoodmini.Business.Food.Food;
import com.example.sellfoodmini.Database.FoodDAO;
import com.example.sellfoodmini.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {

    private final List<Cart_Item> cartItems;
    private final FoodDAO foodDAO;

    public static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemQuantity, itemPrice, itemTotalPrice;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.checkout_item_name);
            itemQuantity = itemView.findViewById(R.id.checkout_item_quantity);
            itemPrice = itemView.findViewById(R.id.checkout_item_price);
            itemTotalPrice = itemView.findViewById(R.id.checkout_item_total_price);
        }
    }

    public CheckoutAdapter(List<Cart_Item> cartItems, FoodDAO foodDAO) {
        this.cartItems = cartItems;
        this.foodDAO = foodDAO;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkout, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        Cart_Item item = cartItems.get(position);
        Food food = foodDAO.selectById(item.getFoodId());

        if (food != null) {

            Locale vietnam = new Locale("vi", "VN");
            NumberFormat formatter = NumberFormat.getNumberInstance(vietnam);
            String itemPrice = formatter.format(food.getPrice()) + "đ";
            String itemTotalPrice = formatter.format((long) food.getPrice() * item.getQuantity()) + "đ";

            holder.itemName.setText(food.getName());
            holder.itemQuantity.setText(String.valueOf(item.getQuantity()));
            holder.itemPrice.setText(itemPrice);
            holder.itemTotalPrice.setText(itemTotalPrice);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }
}