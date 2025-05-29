package com.example.sellfoodmini.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import com.example.sellfoodmini.Business.User.Customer;

import java.util.List;
import java.util.Set;

@Dao
public interface CustomerDAO {
    @Insert
    void insert(Customer customer);

    @Update
    int update(Customer customer);

    @Delete
    int delete(Customer customer);

    @Query("SELECT * FROM Customer")
    List<Customer> selectAll();

    @Query("SELECT * FROM Customer WHERE id = :id")
    Customer selectById(int id);

    @Query("SELECT * FROM Customer WHERE email = :email")
    List<Customer> selectSetById(String email);
}