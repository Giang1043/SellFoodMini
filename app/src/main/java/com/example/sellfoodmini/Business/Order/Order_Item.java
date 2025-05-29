package com.example.sellfoodmini.Business.Order;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import com.example.sellfoodmini.Business.Cart.Cart;
import com.example.sellfoodmini.Business.Food.Food;

import java.io.Serializable;

public class Order_Item implements Serializable {
    private int orderId;
    private int foodId;
    private int quantity;

    public Order_Item() {}
    public Order_Item(int orderId, int foodId, int quantity) {
        this.orderId = orderId;
        this.foodId = foodId;
        this.quantity = quantity;
    }

    public int getOrderId() {return orderId;}
    public void setOrderId(int orderId) {this.orderId = orderId;}

    public int getFoodId() {return foodId;}
    public void setFoodId(int foodId) {this.foodId = foodId;}

    public void setQuantity(int quantity) {this.quantity = quantity;}
    public int getQuantity() {return quantity;}

}
