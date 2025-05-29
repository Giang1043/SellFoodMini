package com.example.sellfoodmini.Business;

import androidx.room.TypeConverter;
import com.example.sellfoodmini.Business.Food.Food;
import com.example.sellfoodmini.Business.Food.FoodRating;
import com.example.sellfoodmini.Business.Order.Order;
import com.example.sellfoodmini.Business.Cart.Cart_Item;
import com.example.sellfoodmini.Business.Order.Order_Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converters {
    private static final Gson gson = new Gson();

    // For List<Food>
    @TypeConverter
    public static List<Food> fromFoodListString(String value) {
        if (value == null) return new ArrayList<>();
        Type listType = new TypeToken<List<Food>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String toFoodListString(List<Food> list) {
        return gson.toJson(list);
    }

    // For List<Order>
    @TypeConverter
    public static List<Order> fromOrderListString(String value) {
        if (value == null) return new ArrayList<>();
        Type listType = new TypeToken<List<Order>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String toOrderListString(List<Order> list) {
        return gson.toJson(list);
    }

    // For List<Cart_Item>
    @TypeConverter
    public static List<Cart_Item> fromCartItemListString(String value) {
        if (value == null || value.isEmpty()) return new ArrayList<>();
        Type listType = new TypeToken<List<Cart_Item>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String toCartItemListString(List<Cart_Item> list) {
        return gson.toJson(list);
    }

    // For List<OrderItem>
    @TypeConverter
    public static List<Order_Item> fromOrderItemListString(String value) {
        if (value == null) return new ArrayList<>();
        Type listType = new TypeToken<List<Order_Item>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String toOrderItemListString(List<Order_Item> list) {
        return gson.toJson(list);
    }

    // For List<FoodRating> (if needed)
    @TypeConverter
    public static List<FoodRating> fromFoodRatingListString(String value) {
        if (value == null) return new ArrayList<>();
        Type listType = new TypeToken<List<FoodRating>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String toFoodRatingListString(List<FoodRating> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}