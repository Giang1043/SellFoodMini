package com.example.sellfoodmini.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import com.example.sellfoodmini.Business.Cart.Cart;

import java.util.List;

@Dao
public interface CartDAO {
    @Insert
    void insert(Cart cart);

    @Update
    int update(Cart cart);

    @Delete
    int delete(Cart cart);

    @Query("SELECT * FROM Cart")
    List<Cart> selectAll();

    @Query("SELECT * FROM Cart WHERE cartId = :id")
    Cart selectById(int id);

    @Query("SELECT * FROM Cart WHERE customerId = :id")
    Cart selectByCustomerId(int id);

    @Query("SELECT * FROM Cart WHERE cartId = :id")
    List<Cart> selectListById(int id);
}