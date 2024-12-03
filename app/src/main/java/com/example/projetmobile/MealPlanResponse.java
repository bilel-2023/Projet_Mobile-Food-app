package com.example.projetmobile;

import java.util.List;

public class MealPlanResponse {
        private List<Meal> meals;
        private Nutrients nutrients;

        public List<Meal> getMeals() {
            return meals;
        }


        public Nutrients getNutrients() {
            return nutrients;
        }

        public static class Meal {
            private int id;
            private String title;
            private int readyInMinutes;
            private int servings;
            private String sourceUrl;

            public int getId() {
                return id;
            }

            public String getTitle() {
                return title;
            }

            public int getReadyInMinutes() {
                return readyInMinutes;
            }

            public int getServings() {
                return servings;
            }

            public String getSourceUrl() {
                return sourceUrl;
            }
        }

        public static class Nutrients {
            private double calories;
            private double protein;
            private double fat;
            private double carbohydrates;

            public double getCalories() {
                return calories;
            }

            public double getProtein() {
                return protein;
            }

            public double getFat() {
                return fat;
            }

            public double getCarbohydrates() {
                return carbohydrates;
            }
        }
    }


