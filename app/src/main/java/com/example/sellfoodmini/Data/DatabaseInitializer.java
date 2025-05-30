package com.example.sellfoodmini.Data;

import android.content.Context;
import android.util.Log;

import com.example.sellfoodmini.Business.Cart.*;
import com.example.sellfoodmini.Business.Food.*;
import com.example.sellfoodmini.Business.Order.*;
import com.example.sellfoodmini.Business.User.*;
import com.example.sellfoodmini.Database.*;

import com.example.sellfoodmini.R;

import java.util.ArrayList;

public class DatabaseInitializer {

    private static final String TAG = "DatabaseInitializer";
    public static void initializeDatabase(Context context) {
        // Build the database
        AppDatabase db = AppDatabase.getInstance(context);

        // Get DAOs
        UserDAO userDAO = db.userDAO();
        OrderDAO orderDAO = db.orderDAO();
        CartDAO cartDAO = db.cartDAO();
        FoodDAO foodDAO = db.foodDAO();

        // Xóa dữ liệu cũ để đảm bảo dữ liệu mẫu được chèn lại
        // db.clearAllTables();

        // Chỉ khởi tạo dữ liệu nếu cơ sở dữ liệu trống
        if (foodDAO.selectAll().isEmpty()) {
            // Insert sample Users (including Customers and Employees)
            Customer customer0 = new Customer("Jane Doe", "abc@example.com", "1234", "0987654321", "123 EE TS");
            Customer customer1 = new Customer("Jane Smith", "jane@example.com", "pass456", "0987654321", "123 Main St");
            Employee employee1 = new Employee("Admin Bob", "bob@example.com", "admin789", "5555555555", "Manager");

            userDAO.insert(customer0);
            userDAO.insert(customer1);
            userDAO.insert(employee1);

            // Insert sample Foods
            Food food1 = new Food(R.drawable.bo_nuong, "Bò nướng", 40000, "Miếng thịt bò tươi ngon, được nướng trên lửa than," +
                    " giữ trọn hương vị ngọt ngào và mềm mại. Lớp ngoài vàng giòn, bên trong thơm lừng," +
                    " hòa quyện cùng gia vị đặc trưng. Được phục vụ với rau sống tươi mát và nước chấm đậm đà.", new ArrayList<>());
            Food food2 = new Food(R.drawable.bun_bo, "Bún bò", 25000, "Món bún bò với nước dùng thanh ngọt, đậm đà," +
                    " kết hợp cùng những lát bò tươi mềm, giò heo thơm ngon. Bún được ăn kèm với rau sống tươi mát," +
                    " gia vị và chanh tạo nên một món ăn vừa cay nồng, vừa thanh khiết, mang đến hương vị đặc biệt khó quên.", new ArrayList<>());
            Food food3 = new Food(R.drawable.com_ga_chien_nho, "Cơm gà chiên", 25000, "Cơm gà chiên vàng giòn, thơm lừng, được chế biến từ gà tươi ngon," +
                    " da giòn rụm mà thịt vẫn mềm ngọt. Cơm được nấu dẻo, thấm đẫm hương vị từ nước luộc gà," +
                    " ăn kèm với rau sống và nước chấm đậm đà, mang đến một bữa ăn đầy đủ và hấp dẫn.", new ArrayList<>());
            Food food4 = new Food(R.drawable.goi_cuon, "Gỏi cuốn", 20000, "Gỏi cuốn tươi ngon, với lớp bánh tráng mềm mịn cuộn bên trong là tôm tươi," +
                    " thịt nạc, rau sống và bún tươi. Món ăn thanh mát, nhẹ nhàng," +
                    " kết hợp với nước chấm mắm nêm hoặc nước tương chua ngọt, tạo nên một hương vị tươi mới, hấp dẫn.", new ArrayList<>());
            Food food5 = new Food(R.drawable.heo_quay, "Heo quay", 35000, "Heo quay giòn rụm, lớp da vàng ươm, thịt bên trong mềm và thơm," +
                    " được tẩm ướp gia vị đậm đà, nướng vừa chín tới. Món ăn được phục vụ nóng hổi, ăn kèm với rau sống tươi mát" +
                    " và nước chấm đặc biệt, mang lại hương vị tuyệt vời, béo ngậy mà không ngấy.", new ArrayList<>());
            Food food6 = new Food(R.drawable.pizza, "Pizza", 45000, "Pizza với lớp vỏ mỏng giòn, phủ đầy phô mai béo ngậy, sốt cà chua thơm lừng" +
                    " và các topping phong phú như thịt xông khói, nấm, rau củ tươi, hay hải sản." +
                    " Món ăn được nướng nóng hổi, mang đến hương vị đậm đà, hòa quyện giữa vị béo, chua, mặn và ngọt, chắc chắn làm hài lòng mọi thực khách.", new ArrayList<>());
            Food food7 = new Food(R.drawable.salad, "Salad", 15000, "Món salad tươi mát với sự kết hợp hoàn hảo giữa các loại rau xanh, trái cây tươi ngon," +
                    " thêm một ít hạt khô giòn rụm. Chấm với nước sốt đặc biệt, mang đến hương vị thanh nhẹ, đầy dinh dưỡng," +
                    " phù hợp cho những ai yêu thích sự tươi mới và nhẹ nhàng.", new ArrayList<>());

            foodDAO.insert(food1);
            foodDAO.insert(food2);
            foodDAO.insert(food3);
            foodDAO.insert(food4);
            foodDAO.insert(food5);
            foodDAO.insert(food6);
            foodDAO.insert(food7);

            Log.d(TAG, "Database initialized with sample data!");
        } else {
            Log.d(TAG, "Database already initialized, skipping sample data insertion.");
        }
    }
}