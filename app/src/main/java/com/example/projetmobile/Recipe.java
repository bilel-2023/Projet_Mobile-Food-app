package com.example.projetmobile;

public class Recipe {
    private String title;
    private String instructions;
    private String imageUrl; // Add the image URL field
    private String category; // Optional, in case you want to use it for filtering
    private int calories; // Add calories field for filtering

    // Getter for title
    public String getTitle() {
        return title;
    }

    // Setter for title
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter for instructions
    public String getInstructions() {
        return instructions;
    }

    // Setter for instructions
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    // Getter for image URL
    public String getImageUrl() {
        return imageUrl;
    }

    // Setter for image URL
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Getter for category (optional, in case it's part of the API response)
    public String getCategory() {
        return category;
    }

    // Setter for category
    public void setCategory(String category) {
        this.category = category;
    }

    // Getter for calories
    public int getCalories() {
        return calories;
    }

    // Setter for calories
    public void setCalories(int calories) {
        this.calories = calories;
    }
}
