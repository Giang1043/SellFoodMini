package com.example.sellfoodmini.Business.Cart;

import java.io.Serializable;

public class Cart_Item implements Serializable {
    private int cartId;
    private int foodId;
    private int quantity;

    public Cart_Item() {}

    public Cart_Item(int cartId, int foodId, int quantity) {
        this.cartId = cartId;
        this.foodId = foodId;
        this.quantity = quantity;
    }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }
    public int getFoodId() { return foodId; }
    public void setFoodId(int foodId) { this.foodId = foodId; }

    public void setQuantity(int quantity) {this.quantity = quantity;}
    public int getQuantity() {return quantity;}
}
