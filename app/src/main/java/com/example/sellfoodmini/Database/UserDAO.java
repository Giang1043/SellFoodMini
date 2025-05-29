package com.example.sellfoodmini.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import com.example.sellfoodmini.Business.User.Customer;
import com.example.sellfoodmini.Business.User.Employee;
import com.example.sellfoodmini.Business.User.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void insert(Customer customer);
    @Insert
    void insert(Employee employee);

    @Update
    int update(Customer customer);
    @Update
    int update(Employee employee);

    @Delete
    int delete(Customer customer);
    @Delete
    int delete(Employee employee);

    @Query("SELECT id, name, email, password, phone FROM Customer")
    List<User> selectAllCustomersAsUsers();

    @Query("SELECT id, name, email, password, phone FROM Employee")
    List<User> selectAllEmployeesAsUsers();

    @Query("SELECT * FROM Customer WHERE id = :id")
    Customer selectCustomerById(int id);

    @Query("SELECT * FROM Employee WHERE id = :id")
    Employee selectEmployeeById(int id);

    @Query("SELECT * FROM Customer WHERE email = :email")
    Customer selectCustomerByEmail(String email);

    @Query("SELECT * FROM Employee WHERE email = :email")
    Employee selectEmployeeByEmail(String email);

    @Query("SELECT EXISTS(SELECT 1 FROM Customer WHERE email = :email AND password = :password) " +
            "OR EXISTS(SELECT 1 FROM Employee WHERE email = :email AND password = :password)")
    boolean checkLogin(String email, String password);

    @Query("SELECT EXISTS(SELECT 1 FROM Customer WHERE email = :email) " +
            "OR EXISTS(SELECT 1 FROM Employee WHERE email = :email)")
    boolean isEmailExists(String email);
}