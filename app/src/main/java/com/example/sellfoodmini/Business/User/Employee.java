package com.example.sellfoodmini.Business.User;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "Employee")
public class Employee extends User{

    private String role;

    public Employee(String name, String email, String password, String phone, String role) {
        super(name,email, password, phone);
        this.role = role;
    }
    @Ignore
    public Employee(int id, String name, String email, String password, String phone, String role) {
        super(id, name,email, password, phone);
        this.role = role;
    }

    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}
}
