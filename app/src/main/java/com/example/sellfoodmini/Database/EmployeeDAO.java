package com.example.sellfoodmini.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import com.example.sellfoodmini.Business.User.Employee;

import java.util.Set;

@Dao
public interface EmployeeDAO {
    @Insert
    boolean insert(Employee Employee);

    @Update
    boolean update(Employee Employee);

    @Delete
    boolean delete(Employee Employee);

    @Query("SELECT * FROM Employee")
    Set<Employee> selectAll();

    @Query("SELECT * FROM Employee WHERE id = :id")
    Employee selectById(int id);

    @Query("SELECT * FROM Employee WHERE email = :email")
    Set<Employee> selectSetById(String email);
}