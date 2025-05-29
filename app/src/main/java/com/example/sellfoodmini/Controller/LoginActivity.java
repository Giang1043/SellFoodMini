package com.example.sellfoodmini.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sellfoodmini.Business.User.Customer;
import com.example.sellfoodmini.Database.AppDatabase;
import com.example.sellfoodmini.Database.UserDAO;
import com.example.sellfoodmini.R;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private TextInputLayout emailLayout, passwordLayout;
    private Button loginButton, registerText;
    private UserDAO userDAO;
    private AppDatabase db;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        // Initialize UI components
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);

        loginButton = findViewById(R.id.login_button);
        registerText = findViewById(R.id.register_text);

        // Initialize Room database and DAO
        db = AppDatabase.getInstance(this); // Assume singleton pattern
        userDAO = db.userDAO();

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    emailLayout.setError("");
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    passwordLayout.setError("");
                }
            }
        });

        loginButton.setOnClickListener(v -> {
            String emailInput = email.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();

            if (emailInput.isEmpty()) {
                emailLayout.setError("Required*");
            }
            if (passwordInput.isEmpty()) {
                passwordLayout.setError("Required*");
            }

            if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                if (userDAO.checkLogin(emailInput, passwordInput)) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    // Find that user
                    Customer customer = userDAO.selectCustomerByEmail(emailInput);
                    // Save customerId to SharedPreferences and set loggedIn to true
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("customerId", customer.getId());
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }

    public static void showLoginConfirmation(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Login Required")
                .setMessage("Do you want to continue?")
                .setPositiveButton("Login", (dialog, which) -> {
                    // Redirect to LoginActivity
                    context.startActivity(new Intent(context, LoginActivity.class));
                    })
                .setNegativeButton("Cancel", (dialog, which) -> {
                })
                .setCancelable(false) // Prevent dismissing by tapping outside
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}