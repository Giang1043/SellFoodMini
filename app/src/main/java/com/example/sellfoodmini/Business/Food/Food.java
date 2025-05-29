package com.example.sellfoodmini.Business.Food;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.sellfoodmini.Business.Converters;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "Food")
@TypeConverters(Converters.class)
public class Food implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int imageResId; // ID của hình ảnh
    private String name;    // Tên món
    private int price;      // Giá món
    @Nullable
    private String description; // Mô tả món
    private List<FoodRating> ratings;

    // Constructor
    public Food(int imageResId, String name, int price, @Nullable String description, List<FoodRating> ratings) {
        this.imageResId = imageResId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.ratings = ratings;
    }

    public void setId(int id) {this.id = id;}

    public int getId() {return id;}
    public int getImageResId() {return imageResId;}

    public String getName() {return name;}

    public int getPrice() {return price;}
    @Nullable
    public String getDescription() {return description;}

    public List<FoodRating> getRatings() {return ratings;}

    public void addRating(FoodRating rating) {
        if (rating.getRating() >= 1 && rating.getRating() <= 5) {
            ratings.add(rating);
            System.out.println("Rating " + rating.getRating() + " added to " + name);
        }
    }
    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (FoodRating rating : ratings) {
            sum += rating.getRating();
        }
        return sum / ratings.size();
    }
}
