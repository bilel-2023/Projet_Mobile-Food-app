package com.example.projetmobile;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class PreferencesActivity extends AppCompatActivity {

    private CheckBox vegetarianCheckBox, veganCheckBox, glutenFreeCheckBox;
    private EditText calorieLimitEditText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preferences);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        vegetarianCheckBox = findViewById(R.id.vegetarianCheckBox);
        veganCheckBox = findViewById(R.id.veganCheckBox);
        glutenFreeCheckBox = findViewById(R.id.glutenFreeCheckBox);
        calorieLimitEditText = findViewById(R.id.calorieLimitEditText);

        sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);

        // Load saved preferences
        loadPreferences();

        findViewById(R.id.savePreferencesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save preferences
                savePreferences();
                Toast.makeText(PreferencesActivity.this, "Preferences saved!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // Load saved preferences
    private void loadPreferences() {
        vegetarianCheckBox.setChecked(sharedPreferences.getBoolean("vegetarian", false));
        veganCheckBox.setChecked(sharedPreferences.getBoolean("vegan", false));
        glutenFreeCheckBox.setChecked(sharedPreferences.getBoolean("glutenFree", false));
        calorieLimitEditText.setText(sharedPreferences.getString("calorieLimit", ""));
    }

    // Save preferences
    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("vegetarian", vegetarianCheckBox.isChecked());
        editor.putBoolean("vegan", veganCheckBox.isChecked());
        editor.putBoolean("glutenFree", glutenFreeCheckBox.isChecked());
        editor.putString("calorieLimit", calorieLimitEditText.getText().toString());
        editor.apply();
    }
}