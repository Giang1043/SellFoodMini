package com.example.sellfoodmini.Business.User;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import com.example.sellfoodmini.Business.Converters;
import com.example.sellfoodmini.Business.Food.Food;
import com.example.sellfoodmini.Business.Food.FoodRating;
import com.example.sellfoodmini.Business.Order.Order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity(tableName = "Customer")
@TypeConverters(Converters.class)
public class Customer extends User{
    @Nullable
    private String address;
    private List<Food> wishlist;

    @Ignore
    public Customer(String name, String email, String password, String phone, @Nullable String address) {
        super(name,email, password, phone);
        this.address = address;
        this.wishlist = new ArrayList<>();
    }
    public Customer(int id, String name, String email, String password, String phone, @Nullable String address) {
        super(id, name,email, password, phone);
        this.address = address;
        this.wishlist = new ArrayList<>();
    }
    @Nullable
    public String getAddress() {return address;}
    public void setAddress(@Nullable String address) {this.address = address;}

    public List<Food> getWishlist() {return wishlist;}
    public void setWishlist(List<Food> wishlist) {this.wishlist = wishlist;}

    public void addToWishlist(Food food) {
        // Check if food with the same ID already exists
        boolean exists = wishlist.stream()
                .anyMatch(existingFood -> existingFood.getId() == food.getId());
        if (!exists) {
            wishlist.add(food);
        }
    }
    public void removeFromWishlist(Food food) {
        // Remove food by ID
        Iterator<Food> iterator = wishlist.iterator();
        while (iterator.hasNext()) {
            Food existingFood = iterator.next();
            if (existingFood.getId() == food.getId()) {
                iterator.remove();
                break; // Assuming IDs are unique, we can break after finding the match
            }
        }
    }

    public void rateFood(Food food, double score) {
        FoodRating rating = new FoodRating(this, food, score);
        food.addRating(rating);
    }
}
