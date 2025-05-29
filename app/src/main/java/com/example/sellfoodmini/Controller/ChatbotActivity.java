package com.example.sellfoodmini.Controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sellfoodmini.Adapter.Chat.ChatAdapter;
import com.example.sellfoodmini.Database.AppDatabase;
import com.example.sellfoodmini.Database.FoodDAO;
import com.example.sellfoodmini.Database.OrderDAO;
import com.example.sellfoodmini.Model.Chat.ChatMessage;
import com.example.sellfoodmini.R;
import com.example.sellfoodmini.Business.Food.Food;
import com.example.sellfoodmini.Business.Order.Order;

import java.util.List;

public class ChatbotActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private EditText userInputEditText;
    private Button sendButton;
    private ChatAdapter chatAdapter;
    private AppDatabase db;
    private FoodDAO foodDAO;
    private OrderDAO orderDAO;
    private int customerId = 1; // Giữ nguyên hardcode như yêu cầu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        // Initialize UI components
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        userInputEditText = findViewById(R.id.userInputEditText);
        sendButton = findViewById(R.id.sendButton);

        // Set up RecyclerView
        chatAdapter = new ChatAdapter();
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Initialize database
        db = AppDatabase.getInstance(this);
        foodDAO = db.foodDAO();
        orderDAO = db.orderDAO();

        // Add welcome message
        chatAdapter.addMessage(new ChatMessage("Hello! I can help you with food queries. Try asking 'What foods are available?', 'Show my orders', or 'Help' for more commands.", false));

        // Handle send button click (đảm bảo chỉ gắn một lần)
        sendButton.setOnClickListener(v -> {
            String userQuery = userInputEditText.getText().toString().trim();
            if (!userQuery.isEmpty()) {
                chatAdapter.addMessage(new ChatMessage(userQuery, true));
                processQuery(userQuery);
                userInputEditText.setText("");
                chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
            }
        });
    }

    private void processQuery(String query) {
        String lowerQuery = query.toLowerCase();
        String response;

        // Truy vấn 1: Danh sách món ăn
        if (lowerQuery.contains("food") && lowerQuery.contains("available")) {
            List<Food> foods = foodDAO.selectAll();
            if (foods.isEmpty()) {
                response = "No foods are available at the moment.";
            } else {
                StringBuilder foodList = new StringBuilder("Available foods:\n");
                for (Food food : foods) {
                    foodList.append("- ").append(food.getName()).append(" (").append(food.getPrice()).append(")\n");
                }
                response = foodList.toString();
            }
        }
        // Truy vấn 2: Hiển thị đơn hàng
        else if (lowerQuery.contains("show") && lowerQuery.contains("orders")) {
            List<Order> orders = orderDAO.selectSetByCustomer(customerId);
            if (orders.isEmpty()) {
                response = "You have no orders.";
            } else {
                StringBuilder orderList = new StringBuilder("Your orders:\n");
                for (Order order : orders) {
                    orderList.append("- Order ID: ").append(order.getOrderId()).append(", Total: ").append(order.getTotalCost()).append("\n");
                }
                response = orderList.toString();
            }
        }
        // Truy vấn 3: Giá của một món ăn cụ thể
        else if (lowerQuery.contains("price of")) {
            String foodName = extractFoodName(lowerQuery, "price of");
            if (foodName == null) {
                response = "Please specify a food name (e.g., 'What is the price of Pizza?').";
            } else {
                Food food = findFoodByName(foodName);
                if (food == null) {
                    response = "Sorry, I couldn't find a food named '" + foodName + "'.";
                } else {
                    response = "The price of " + food.getName() + " is " + food.getPrice() + ".";
                }
            }
        }
        // Truy vấn 4: Kiểm tra khả năng đặt hàng món ăn
        else if (lowerQuery.contains("can i order")) {
            String foodName = extractFoodName(lowerQuery, "can i order");
            if (foodName == null) {
                response = "Please specify a food name (e.g., 'Can I order Pizza?').";
            } else {
                Food food = findFoodByName(foodName);
                if (food == null) {
                    response = "Sorry, I couldn't find a food named '" + foodName + "'.";
                } else {
                    response = "Yes, you can order " + food.getName() + "! It costs " + food.getPrice() + ".";
                }
            }
        }
        // Truy vấn 5: Tổng chi phí đơn hàng
        else if (lowerQuery.contains("total cost of my orders")) {
            List<Order> orders = orderDAO.selectSetByCustomer(customerId);
            if (orders.isEmpty()) {
                response = "You have no orders.";
            } else {
                double totalCost = 0;
                for (Order order : orders) {
                    totalCost += order.getTotalCost();
                }
                response = "The total cost of your orders is " + totalCost + ".";
            }
        }
        // Truy vấn 6: Số lượng đơn hàng
        else if (lowerQuery.contains("how many orders")) {
            List<Order> orders = orderDAO.selectSetByCustomer(customerId);
            response = "You have " + orders.size() + " orders.";
        }
        // Truy vấn 7: Món ăn đắt nhất
        else if (lowerQuery.contains("most expensive food")) {
            List<Food> foods = foodDAO.selectAll();
            if (foods.isEmpty()) {
                response = "No foods are available at the moment.";
            } else {
                Food mostExpensive = foods.get(0);
                for (Food food : foods) {
                    if (food.getPrice() > mostExpensive.getPrice()) {
                        mostExpensive = food;
                    }
                }
                response = "The most expensive food is " + mostExpensive.getName() + " at " + mostExpensive.getPrice() + ".";
            }
        }
        // Truy vấn 8: Hiển thị danh sách lệnh
        else if (lowerQuery.contains("help")) {
            response = "I can help with the following commands:\n" +
                    "- What foods are available?\n" +
                    "- Show my orders\n" +
                    "- What is the price of [food name]?\n" +
                    "- Can I order [food name]?\n" +
                    "- What is the total cost of my orders?\n" +
                    "- How many orders do I have?\n" +
                    "- What is the most expensive food?\n" +
                    "- Help (to see this list)";
        }
        // Truy vấn mặc định
        else {
            response = "Sorry, I didn't understand your query. Try asking 'What foods are available?' or 'Help' for more commands.";
        }

        // Chỉ thêm một tin nhắn phản hồi
        chatAdapter.addMessage(new ChatMessage(response, false));
    }

    // Hàm hỗ trợ tìm món ăn theo tên
    private Food findFoodByName(String foodName) {
        List<Food> foods = foodDAO.selectAll();
        for (Food food : foods) {
            if (food.getName().toLowerCase().contains(foodName.toLowerCase())) {
                return food;
            }
        }
        return null;
    }

    // Hàm hỗ trợ trích xuất tên món ăn từ truy vấn
    private String extractFoodName(String query, String prefix) {
        int startIndex = query.indexOf(prefix) + prefix.length();
        String foodPart = query.substring(startIndex).trim();
        if (foodPart.isEmpty()) return null;
        // Loại bỏ dấu '?' nếu có
        if (foodPart.endsWith("?")) {
            foodPart = foodPart.substring(0, foodPart.length() - 1).trim();
        }
        return foodPart.isEmpty() ? null : foodPart;
    }
}