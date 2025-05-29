package com.example.sellfoodmini.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import com.example.sellfoodmini.Business.Order.Order;

import java.util.List;

@Dao
public interface OrderDAO {
    @Insert
    void insert(Order order);

    @Update
    int update(Order order);

    @Delete
    int delete(Order order);

    @Query("SELECT * FROM `Order`")
    List<Order> selectAll();

    @Query("SELECT * FROM `Order` WHERE orderId = :id")
    Order selectById(int id);

    @Query("SELECT orderId FROM `Order` WHERE customerId = :customerId ORDER BY orderId DESC LIMIT 1")
    int getNewestOrderId(int customerId);

    @Query("SELECT * FROM `Order` WHERE customerId = :id")
    List<Order> selectSetByCustomer(int id);
}