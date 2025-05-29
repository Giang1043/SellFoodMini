package com.example.sellfoodmini.Business.Order;

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

import com.example.sellfoodmini.Controller.OrderDetailFragment;
import com.example.sellfoodmini.R;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private FragmentActivity activity;

    public OrderAdapter(List<Order> orderList, FragmentActivity activity) {
        this.orderList = orderList;
        this.activity = activity;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderStatus, orderDate;
        Button btnViewDetail;
        MaterialCardView orderStatusBar;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderDate = itemView.findViewById(R.id.orderDate);
            btnViewDetail = itemView.findViewById(R.id.btnViewDetail);
            orderStatusBar = itemView.findViewById(R.id.orderStatusBar);
        }
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_orderhistory, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Set order details
        holder.orderId.setText(String.valueOf(order.getOrderId()));
        holder.orderStatus.setText(order.getOrderStatus().getStatus());

        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy h:mm a", new Locale("vi", "VN"));
        String formattedDate = dateFormat.format(order.getOrderDate());
        holder.orderDate.setText(formattedDate);

        // Set status text color and stroke color based on OrderStatus
        switch (order.getOrderStatus()) {
            case CHO_XU_LY:
                holder.orderStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_light));
                holder.orderStatusBar.setStrokeColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_light));
                break;
            case DANG_GIAO:
                holder.orderStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_purple));
                holder.orderStatusBar.setStrokeColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_purple));
                break;
            case DA_GIAO:
                holder.orderStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_light));
                holder.orderStatusBar.setStrokeColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_light));
                break;
            case DA_HUY:
                holder.orderStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_light));
                holder.orderStatusBar.setStrokeColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_light));
                break;
        }

        // Handle "View Detail" button click
        holder.btnViewDetail.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Order selectedOrder = orderList.get(adapterPosition);
                OrderDetailFragment detailFragment = new OrderDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("order", selectedOrder);
                detailFragment.setArguments(bundle);

                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public void updateOrderList(List<Order> newOrderList) {
        this.orderList = newOrderList;
        notifyDataSetChanged();
    }
}