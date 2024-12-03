package com.example.projetmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealPlanActivity extends AppCompatActivity {

    public static final String API_KEY = "e07415f8c25b4833a62f2b3fc7cf0626";
    private ListView mealListView;
    private ArrayAdapter<String> adapter;
    private List<RecipeSearchResponse.Recipe> recipes;
    private List<RecipeSearchResponse.Recipe> filteredRecipes;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        mealListView = findViewById(R.id.meal_list_view);
        EditText searchBar = findViewById(R.id.searchBar);

        recipes = new ArrayList<>();
        filteredRecipes = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        mealListView.setAdapter(adapter);

        sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);

        // Fetch user preferences
        String dietPreference = getUserDietPreference();
        String calorieLimit = sharedPreferences.getString("calorieLimit", "");

        SpoonacularApi api = RetrofitClient.getRetrofitInstance().create(SpoonacularApi.class);
        fetchMoreRecipes(api, dietPreference, calorieLimit);

        mealListView.setOnItemClickListener((parent, view, position, id) -> {
            RecipeSearchResponse.Recipe selectedRecipe = filteredRecipes.get(position);
            Intent intent = new Intent(MealPlanActivity.this, RecipeDetailsActivity.class);
            intent.putExtra("RECIPE_ID", selectedRecipe.getId());
            intent.putExtra("RECIPE_TITLE", selectedRecipe.getTitle());
            intent.putExtra("RECIPE_IMAGE", selectedRecipe.getImageUrl());
            startActivity(intent);
        });

        // Add text watcher to filter recipes based on search input
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterRecipes(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private String getUserDietPreference() {
        StringBuilder diet = new StringBuilder();

        if (sharedPreferences.getBoolean("vegetarian", false)) {
            diet.append("vegetarian,");
        }
        if (sharedPreferences.getBoolean("vegan", false)) {
            diet.append("vegan,");
        }
        if (sharedPreferences.getBoolean("glutenFree", false)) {
            diet.append("glutenFree,");
        }

        if (diet.length() > 0) {
            diet.deleteCharAt(diet.length() - 1);  // Remove last comma
        }

        return diet.toString();
    }

    private void fetchMoreRecipes(SpoonacularApi api, String dietPreference, String calorieLimit) {
        Call<RecipeSearchResponse> call = api.searchRecipes(dietPreference, 20, API_KEY);
        call.enqueue(new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipes.clear();
                    filteredRecipes.clear();
                    adapter.clear();

                    for (RecipeSearchResponse.Recipe recipe : response.body().getResults()) {
                        // Check if recipe's calories are within the limit
                        if (calorieLimit != null && !calorieLimit.isEmpty()) {
                            try {
                                int calorieLimitInt = Integer.parseInt(calorieLimit);
                                if (recipe.getCalories() <= calorieLimitInt) {
                                    recipes.add(recipe);
                                    filteredRecipes.add(recipe);
                                    adapter.add(recipe.getTitle());
                                }
                            } catch (NumberFormatException e) {
                                Log.e("MealPlanActivity", "Invalid calorie limit: " + calorieLimit);
                            }
                        } else {
                            // If no calorie limit, add all recipes
                            recipes.add(recipe);
                            filteredRecipes.add(recipe);
                            adapter.add(recipe.getTitle());
                        }
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("MealPlanActivity", "Failed to load recipes: " + response.message());
                    Toast.makeText(MealPlanActivity.this, "Failed to load recipes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {
                Log.e("MealPlanActivity", "Error: " + t.getMessage());
                Toast.makeText(MealPlanActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterRecipes(String query) {
        List<String> filteredTitles = new ArrayList<>();
        filteredRecipes.clear(); // Clear filtered recipes before adding new ones
        for (RecipeSearchResponse.Recipe recipe : recipes) {
            if (recipe.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredRecipes.add(recipe);
                filteredTitles.add(recipe.getTitle());
            }
        }

        adapter.clear();
        adapter.addAll(filteredTitles);
        adapter.notifyDataSetChanged();
    }
}
