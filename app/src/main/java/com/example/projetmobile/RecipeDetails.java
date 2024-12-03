package com.example.projetmobile;

import java.util.List;

public class RecipeDetails {
    private String title;
    private String summary;
    private String image;
    private List<Ingredient> extendedIngredients;
    private List<Instruction> analyzedInstructions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Ingredient> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<Ingredient> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }

    public List<Instruction> getAnalyzedInstructions() {
        return analyzedInstructions;
    }

    public void setAnalyzedInstructions(List<Instruction> analyzedInstructions) {
        this.analyzedInstructions = analyzedInstructions;
    }

    // Instruction class to hold instruction data
    public static class Instruction {
        private String name; // Name of the instruction set (optional, e.g., "Main Course Steps")
        private List<Step> steps;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Step> getSteps() {
            return steps;
        }

        public void setSteps(List<Step> steps) {
            this.steps = steps;
        }

        @Override
        public String toString() {
            return name != null ? name : "Instructions";
        }
    }

    // Step class to hold individual steps in instructions
    public static class Step {
        private int number; // Step number
        private String step; // Description of the step

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }

        @Override
        public String toString() {
            return number + ". " + step;
        }
    }
}
