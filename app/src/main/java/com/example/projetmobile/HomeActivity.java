package com.example.projetmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class HomeActivity extends AppCompatActivity {

    private CardView mealPlanCard, preferencesCard, nutritionalDataCard;
    private ImageButton profile,settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        profile=findViewById(R.id.profile);
        settings=findViewById(R.id.settings);
/*
        // Initialize the CardViews
        mealPlanCard = findViewById(R.id.meal_plan_card);
        preferencesCard = findViewById(R.id.preferences_card);
        nutritionalDataCard = findViewById(R.id.nutritional_data_card);

        // Set OnClickListener for each CardView
        mealPlanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Meal Plan Activity
                startActivity(new Intent(HomeActivity.this, MealPlanActivity.class));
            }
        });

        preferencesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Preferences Activity
                startActivity(new Intent(HomeActivity.this, PreferencesActivity.class));
            }
        });

        nutritionalDataCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Nutritional Data Activity
                startActivity(new Intent(HomeActivity.this, NutritionalDataActivity.class));
            }
        });*/
        LinearLayout mealPlansLayout = findViewById(R.id.mealPlansLayout);
        mealPlansLayout.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MealPlanActivity.class));
        });

        LinearLayout barcodeScannerLayout = findViewById(R.id.barcodeScannerLayout);
        barcodeScannerLayout.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, CodeBar.class));
        });

        LinearLayout supermarketsLayout = findViewById(R.id.supermarketsLayout);
        supermarketsLayout.setOnClickListener(v -> {
            // Format the URI to search for "supermarkets" near the current location
            Uri geoUri = Uri.parse("geo:0,0?q=supermarkets");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);

            // Ensure there is an app to handle the intent
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(HomeActivity.this, "No application available to view maps.", Toast.LENGTH_SHORT).show();
            }
        });



        LinearLayout chatbotLayout = findViewById(R.id.chatbotLayout);
        chatbotLayout.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, Chatbot.class));
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Profile.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, PreferencesActivity.class));
            }
        });

    }
}
