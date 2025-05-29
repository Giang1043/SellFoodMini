package com.example.sellfoodmini.Business.Order;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.sellfoodmini.Business.Converters;
import com.example.sellfoodmini.Business.User.Customer;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(tableName = "Order",
        foreignKeys = @ForeignKey(entity = Customer.class,
                parentColumns = "id",
                childColumns = "customerId"))
@TypeConverters(Converters.class)
public class Order implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int orderId;
    private int customerId;
    private List<Order_Item> items;
    private double totalCost;
    private Date orderDate;
    private OrderStatus orderStatus;

    public Order(int customerId, List<Order_Item> items, double totalCost, OrderStatus orderStatus) {
        this.customerId = customerId;
        this.items = items;
        this.totalCost = totalCost;
        this.orderDate = new Date(); // Capture order timestamp
        this.orderStatus = orderStatus;
    }
    @Ignore
    public Order(int orderId, int customerId, List<Order_Item> items, double totalCost, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.totalCost = totalCost;
        this.orderDate = new Date(); // Capture order timestamp
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {return orderId;}
    public void setOrderId(int orderId) {this.orderId = orderId;}

    public int getCustomerId() {return customerId;}

    public void setCustomerId(int customerId) {this.customerId = customerId;}

    public List<Order_Item> getItems() {return items;}
    public void setItems(List<Order_Item> items) {this.items = items;}

    public double getTotalCost() {return totalCost;}
    public void setTotalCost(double totalCost) {this.totalCost = totalCost;}

    public void setOrderDate(Date orderDate) {this.orderDate = orderDate;}
    public Date getOrderDate() {return orderDate;}

    public OrderStatus getOrderStatus() {return orderStatus;}
    public void setOrderStatus(OrderStatus orderStatus) {this.orderStatus = orderStatus;}
}