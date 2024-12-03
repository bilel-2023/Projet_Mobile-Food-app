package com.example.projetmobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

public class ProductScannedDetails extends AppCompatActivity {
    private TextView productNameTextView;
    private TextView ingredientsTextView;
    private TextView nutritionGradeTextView;
    private ImageView productImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_scanned_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the UI components
        productNameTextView = findViewById(R.id.product_name_text_view);
        ingredientsTextView = findViewById(R.id.ingredients_text_view);
        nutritionGradeTextView = findViewById(R.id.nutrition_grade_text_view);
        productImageView = findViewById(R.id.product_image_view);
        Button backButton = findViewById(R.id.back_button); // Initialize the back button

        // Get the product information from the Intent
        String productName = getIntent().getStringExtra("product_name");
        String ingredients = getIntent().getStringExtra("ingredients");
        String nutritionGrade = getIntent().getStringExtra("nutrition_grade");
        String imageUrl = getIntent().getStringExtra("image_url");

        // Display the product information
        productNameTextView.setText(productName);
        ingredientsTextView.setText(ingredients);
        nutritionGradeTextView.setText(nutritionGrade);

        // Load the product image
        Picasso.get().load(imageUrl).into(productImageView);

        // Set OnClickListener for the back button
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductScannedDetails.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Optional: Call finish() to remove this activity from the back stack
        });
    }
}