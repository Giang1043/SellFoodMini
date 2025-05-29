package com.example.sellfoodmini.Business.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellfoodmini.Business.Food.Food;
import com.example.sellfoodmini.Controller.ProductDetailFragment;
import com.example.sellfoodmini.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {
    private List<Food> wishlist;
    private FragmentActivity activity;
    private OnWishlistItemClickListener listener;

    public interface OnWishlistItemClickListener {
        void onDeleteClick(Food food);
    }

    public WishlistAdapter(List<Food> wishlist, FragmentActivity activity, OnWishlistItemClickListener listener) {
        this.wishlist = wishlist;
        this.activity = activity;
        this.listener = listener;
    }
    public static class WishlistViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        Button deleteButton;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.wishlist_item_image);
            foodName = itemView.findViewById(R.id.wishlist_item_name);
            foodPrice = itemView.findViewById(R.id.wishlist_item_price);
            deleteButton = itemView.findViewById(R.id.wishlist_item_delete);
        }
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wishlist, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Food food = wishlist.get(position);

        // Set food details
        holder.foodName.setText(food.getName());

        Locale vietnam = new Locale("vi", "VN");
        NumberFormat formatter = NumberFormat.getNumberInstance(vietnam);
        String formattedPrice = formatter.format(food.getPrice()) + "đ";
        holder.foodPrice.setText(formattedPrice);

        holder.foodImage.setImageResource(food.getImageResId());

        // Click listener for item to go to ProductDetailFragment
        holder.itemView.setOnClickListener(v -> {
            ProductDetailFragment detailFragment = new ProductDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("food", food);
            detailFragment.setArguments(bundle);

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Delete button click listener
        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(food);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }

    public void updateWishlist(List<Food> newWishlist) {
        this.wishlist = newWishlist;
        notifyDataSetChanged();
    }
}