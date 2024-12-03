package com.example.projetmobile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface SpoonacularApi {

    @GET("recipes/complexSearch")
    Call<RecipeSearchResponse> searchRecipes(
            @Query("diet") String diet,
            @Query("number") int number,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/complexSearch")
    Call<RecipeResponse> getRecipesByCategory(
            @Query("query") String query,
            @Query("apiKey") String apiKey
    );


    // Get Recipe Information
    @GET("recipes/{id}/information")
    Call<RecipeDetails> getRecipeDetails(
            @Path("id") int id,
            @Query("apiKey") String apiKey
    );

    // Generate Meal Plan
    @GET("mealplanner/generate")
    Call<MealPlanResponse> generateMealPlan(
            @Query("timeFrame") String timeFrame,
            @Query("targetCalories") int targetCalories,
            @Query("diet") String diet,
            @Query("apiKey") String apiKey
    );

    @GET("food/converse")
    Call<ChatbotResponse> converse(
            @Query("text") String text,
            @Query("contextId") String contextId
    );
}
