package com.example.sellfoodmini.Business.Food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sellfoodmini.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FoodAdapter extends BaseAdapter {
    private final Context context;
    private List<Food> foodList;

    public FoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.productImage);
            holder.textViewName = convertView.findViewById(R.id.productName);
            holder.textViewPrice = convertView.findViewById(R.id.productPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy thông tin món ăn tại vị trí position
        Food food = foodList.get(position);

        // Hiển thị thông tin lên view
        holder.imageView.setImageResource(food.getImageResId());
        holder.textViewName.setText(food.getName());

        Locale vietnam = new Locale("vi", "VN");
        NumberFormat formatter = NumberFormat.getNumberInstance(vietnam);
        String formattedPrice = formatter.format(food.getPrice()) + "đ";
        holder.textViewPrice.setText(formattedPrice);

        return convertView;
    }

    public void SelectFood(List<Food> newFoodList) {
        this.foodList = newFoodList;
        notifyDataSetChanged(); // Cập nhật lại giao diện
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewPrice;
    }
}
