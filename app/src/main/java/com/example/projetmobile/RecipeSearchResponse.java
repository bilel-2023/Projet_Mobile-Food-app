package com.example.projetmobile;

import java.util.List;

public class RecipeSearchResponse {

    private List<Recipe> results;

    public List<Recipe> getResults() {
        return results;
    }

    public void setResults(List<Recipe> results) {
        this.results = results;
    }

    public static class Recipe {
        private int id;
        private String title;
        private String image;
        private Integer calories;  // Added field to hold the calories information

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * Get the image URL.
         * If the image field is not null or empty, return it as is.
         * Otherwise, return a placeholder or default image URL.
         */
        public String getImageUrl() {
            if (image != null && !image.isEmpty()) {
                return image;
            } else {
                // Return a default placeholder image if image is null or empty
                return "https://example.com/chef.png";
            }
        }

        public void setImage(String image) {
            this.image = image;
        }

        // Getter for calories
        public Integer getCalories() {
            if (calories != null) {
                return calories;
            } else {
                return 0;  // Return 0 if calories information is not available
            }
        }

        // Setter for calories
        public void setCalories(Integer calories) {
            this.calories = calories;
        }
    }
}
