package com.example.sellfoodmini.Business.User;

import androidx.annotation.Nullable;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    protected int id;
    protected String name;
    protected String email;
    protected String password;
    @Nullable
    protected String phone;

    public User(){};

    public User(String name, String email, String password, @Nullable String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
    @Ignore
    public User(int id, String name, String email, String password, @Nullable String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    @Nullable
    public String getPhone() {return phone;}
    public void setPhone(@Nullable String phone) {this.phone = phone;}
}
