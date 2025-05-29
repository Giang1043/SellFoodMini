package com.example.sellfoodmini.Business.Cart;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.sellfoodmini.Business.Converters;
import com.example.sellfoodmini.Business.Food.Food;
import com.example.sellfoodmini.Business.User.Customer;
import com.example.sellfoodmini.Database.FoodDAO;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "Cart",
        foreignKeys = @ForeignKey(entity = Customer.class,
                parentColumns = "id",
                childColumns = "customerId"))
@TypeConverters(Converters.class)
public class Cart implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int cartId;
    private int customerId;
    private List<Cart_Item> items;

    public Cart(int customerId, List<Cart_Item> items) {
        this.customerId = customerId;
        this.items = items;
    }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getCustomerId() {return customerId;}
    public void setCustomerId(int customerId) {this.customerId = customerId;}

    public List<Cart_Item> getItems() { return items; }
    public void setItems(List<Cart_Item> items) {this.items = items;}

    public double getTotalCartCost(FoodDAO foodDAO) {
        double total = 0;
        if (!items.isEmpty()) {
            for (Cart_Item item : items) {
                Food food = foodDAO.selectById(item.getFoodId());
                if (food != null) {
                    total += food.getPrice() * item.getQuantity();
                }
            }
        }
        return total;
    }
}