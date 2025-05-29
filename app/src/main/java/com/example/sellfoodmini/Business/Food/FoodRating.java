package com.example.sellfoodmini.Business.Food;

import com.example.sellfoodmini.Business.User.Customer;

public class FoodRating {
    private Customer customer;
    private Food food;
    private double rating;

    public FoodRating(Customer customer, Food food, double rating) {
        this.customer = customer;
        this.food = food;
        this.rating = rating;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Food getFood() {
        return food;
    }

    public double getRating() {
        return rating;
    }
}
