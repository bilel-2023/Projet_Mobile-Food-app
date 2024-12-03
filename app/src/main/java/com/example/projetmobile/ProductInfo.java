package com.example.projetmobile;

public class ProductInfo {
    private String code;
    private Product product;

    public String getCode() {
        return code;
    }

    public Product getProduct() {
        return product;
    }

    public static class Product {
        private String product_name;
        private String ingredients_text;
        private String nutrition_grade_fr;
        private String image_url;

        public String getProductName() {
            return product_name;
        }

        public String getIngredientsText() {
            return ingredients_text;
        }

        public String getNutritionGrade() {
            return nutrition_grade_fr;
        }

        public String getImageUrl() {
            return image_url;
        }
    }
}