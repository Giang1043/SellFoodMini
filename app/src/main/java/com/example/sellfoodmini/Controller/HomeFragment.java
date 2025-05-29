package com.example.sellfoodmini.Controller;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sellfoodmini.Business.Food.Food;
import com.example.sellfoodmini.Business.Food.FoodAdapter;
import com.example.sellfoodmini.Database.AppDatabase;
import com.example.sellfoodmini.Database.FoodDAO;
import com.example.sellfoodmini.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment {

    private GridView gridView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;
    private EditText editTextSearch;
    private Button btnSearch; // Added Button reference
    private FoodDAO foodDAO;
    private AppDatabase db;

    public HomeFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(requireContext());
        foodDAO = db.foodDAO();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ các view
        editTextSearch = view.findViewById(R.id.editTextSearch);
        btnSearch = view.findViewById(R.id.btnSearch); // Initialize the search button

        // Lấy danh sách món ăn
        foodList = foodDAO.selectAll();
        if (foodList == null) {
            foodList = new ArrayList<>();
        }

        // Ánh xạ GridView
        gridView = view.findViewById(R.id.gridView);
        foodAdapter = new FoodAdapter(requireContext(), foodList);
        gridView.setAdapter(foodAdapter);

        // Xử lý khi nhấn nút Search
        btnSearch.setOnClickListener(v -> {
            String query = editTextSearch.getText().toString();
            if (query.isEmpty()) {
                foodAdapter.SelectFood(foodList); // Show full list if search_input is empty
            } else {
                filterData(query);
            }
        });
        // Xử lý khi nhấn Enter để search
        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                filterData(editTextSearch.getText().toString());
                return true;
            }
            return false;
        });

        // Xử lý khi nhấn vào một sản phẩm
        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            Food selectedFood = (Food) parent.getAdapter().getItem(position);

            // Tạo Fragment mới
            ProductDetailFragment detailFragment = new ProductDetailFragment();

            // Gửi dữ liệu qua Bundle
            Bundle bundle = new Bundle();
            bundle.putSerializable("food", selectedFood);
            detailFragment.setArguments(bundle);

            // Mở Fragment mới
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    // Lọc dữ liệu theo từ khóa tìm kiếm
    private void filterData(String query) {
        List<Food> filteredList = new ArrayList<>();
        String normalizedQuery = normalizeString(query).toLowerCase(); // Chuẩn hóa và chuyển thành chữ thường

        for (Food food : foodList) {
            if (food.getName() != null) {
                String normalizedName = normalizeString(food.getName()).toLowerCase();
                if (normalizedName.contains(normalizedQuery)) {
                    filteredList.add(food);
                }
            }
        }
        foodAdapter.SelectFood(filteredList);
    }

    // Hàm chuẩn hóa chuỗi: loại bỏ dấu
    private String normalizeString(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD); // Phân tách ký tự và dấu
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+"); // Regex để xóa dấu
        return pattern.matcher(normalized).replaceAll("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}