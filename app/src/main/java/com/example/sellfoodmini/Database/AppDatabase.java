package com.example.sellfoodmini.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.sellfoodmini.Business.Cart.Cart_Item;
import com.example.sellfoodmini.Business.Converters;
import com.example.sellfoodmini.Business.Order.Order_Item;
import com.example.sellfoodmini.Business.User.Customer;
import com.example.sellfoodmini.Business.User.Employee;
import com.example.sellfoodmini.Business.Order.Order;
import com.example.sellfoodmini.Business.Food.Food;
import com.example.sellfoodmini.Business.Cart.Cart;

@Database(entities = {Customer.class, Employee.class, Food.class, Order.class, Cart.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    // Define DAOs
    public abstract UserDAO userDAO();
    public abstract CustomerDAO customerDAO();
    public abstract OrderDAO orderDAO();
    public abstract CartDAO cartDAO();
    public abstract FoodDAO foodDAO();

    private static AppDatabase instance;
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "sellfoodmini_db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
    public void close() {
        if (instance != null && instance.isOpen()) {
            instance.close();
            instance = null;
        }
    }
}
