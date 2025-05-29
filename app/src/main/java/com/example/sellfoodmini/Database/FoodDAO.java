package com.example.sellfoodmini.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import com.example.sellfoodmini.Business.Food.Food;

import java.util.List;
import java.util.Set;

@Dao
public interface FoodDAO {
    @Insert
    void insert(Food Food);

    @Update
    int update(Food Food);

    @Delete
    int delete(Food Food);

    @Query("SELECT * FROM Food")
    List<Food> selectAll();

    @Query("SELECT * FROM Food WHERE id= :id")
    Food selectById(int id);

    @Query("SELECT * FROM Food WHERE name = :name")
    List<Food> selectSetById(String name);
}