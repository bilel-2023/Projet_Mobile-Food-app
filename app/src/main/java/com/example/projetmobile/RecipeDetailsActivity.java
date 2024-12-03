package com.example.projetmobile;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.text.Html;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {

    private TextView tvRecipeTitle, tvRecipeSummary;
    private ImageView ivRecipeImage;
    private ListView lvIngredients, lvInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // Initialize views
        tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        tvRecipeSummary = findViewById(R.id.tvRecipeSummary);
        ivRecipeImage = findViewById(R.id.ivRecipeImage);
        lvIngredients = findViewById(R.id.lvIngredients);
        lvInstructions = findViewById(R.id.lvInstructions);

        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);

        if (recipeId != -1) {
            SpoonacularApi api = RetrofitClient.getRetrofitInstance().create(SpoonacularApi.class);
            fetchRecipeDetails(api, recipeId);
        } else {
            Log.e("Error", "No recipe ID passed");
        }
    }

    private void fetchRecipeDetails(SpoonacularApi api, int recipeId) {
        Call<RecipeDetails> call = api.getRecipeDetails(recipeId, MealPlanActivity.API_KEY);
        call.enqueue(new Callback<RecipeDetails>() {
            @Override
            public void onResponse(Call<RecipeDetails> call, Response<RecipeDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecipeDetails recipe = response.body();

                    // Set the recipe title
                    tvRecipeTitle.setText(recipe.getTitle());

                    // Clean and set the recipe summary (convert HTML to plain text)
                    String summary = recipe.getSummary();
                    if (summary != null && !summary.isEmpty()) {
                        String plainSummary = Html.fromHtml(summary, Html.FROM_HTML_MODE_LEGACY).toString();
                        tvRecipeSummary.setText(plainSummary);
                    } else {
                        tvRecipeSummary.setText("No summary available.");
                    }

                    // Load the recipe image using Picasso
                    Picasso.get().load(recipe.getImage()).into(ivRecipeImage);

                    // Set ingredients list
                    ArrayAdapter<Ingredient> ingredientsAdapter = new ArrayAdapter<>(
                            RecipeDetailsActivity.this,
                            android.R.layout.simple_list_item_1,
                            recipe.getExtendedIngredients());
                    lvIngredients.setAdapter(ingredientsAdapter);
                    adjustListViewHeightBasedOnChildren(lvIngredients);

                    // Parse and set instructions list
                    List<String> instructionSteps = new ArrayList<>();
                    if (recipe.getAnalyzedInstructions() != null && !recipe.getAnalyzedInstructions().isEmpty()) {
                        for (RecipeDetails.Instruction instruction : recipe.getAnalyzedInstructions()) {
                            if (instruction.getSteps() != null) {
                                for (RecipeDetails.Step step : instruction.getSteps()) {
                                    instructionSteps.add(step.getNumber() + ". " + step.getStep());
                                }
                            }
                        }
                    } else {
                        instructionSteps.add("No instructions available.");
                    }

                    ArrayAdapter<String> instructionsAdapter = new ArrayAdapter<>(
                            RecipeDetailsActivity.this,
                            android.R.layout.simple_list_item_1,
                            instructionSteps);
                    lvInstructions.setAdapter(instructionsAdapter);
                    adjustListViewHeightBasedOnChildren(lvInstructions);

                } else {
                    Log.e("Error", "Failed to fetch recipe details");
                }
            }

            @Override
            public void onFailure(Call<RecipeDetails> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void adjustListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) return;

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
