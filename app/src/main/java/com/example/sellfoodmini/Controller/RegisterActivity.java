package com.example.sellfoodmini.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sellfoodmini.Business.Cart.Cart;
import com.example.sellfoodmini.Business.User.Customer;
import com.example.sellfoodmini.Database.AppDatabase;
import com.example.sellfoodmini.Database.CartDAO;
import com.example.sellfoodmini.Database.UserDAO;
import com.example.sellfoodmini.R;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, email, password, confirmPassword;
    private Button registerButton, loginText;
    private AppDatabase db;
    private UserDAO userDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.register_button);
        loginText = findViewById(R.id.login_text);

        db = AppDatabase.getInstance(this);
        userDAO = db.userDAO();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameInput = name.getText().toString().trim();
                String emailInput = email.getText().toString().trim();
                String passwordInput = password.getText().toString().trim();
                String confirmPasswordInput = confirmPassword.getText().toString().trim();

                if (nameInput.isEmpty() || emailInput.isEmpty() ||
                        passwordInput.isEmpty() || confirmPasswordInput.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!passwordInput.equals(confirmPasswordInput)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                } else if(userDAO.isEmailExists(emailInput)){
                    Toast.makeText(RegisterActivity.this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
                } else {
                    db.runInTransaction(() -> {
                        try {
                            Customer newCustomer = new Customer(nameInput, emailInput, passwordInput, null, null);
                            userDAO.insert(newCustomer); // Insert Customer

                            runOnUiThread(() -> {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            });
                        } catch (Exception e) {
                            Toast.makeText(RegisterActivity.this, "Lỗi! Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
