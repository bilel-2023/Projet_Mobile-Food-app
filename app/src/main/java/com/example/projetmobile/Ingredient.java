package com.example.projetmobile;

public class Ingredient {
    private String name;
    private double amount;
    private String unit;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    // Override toString to display ingredient details
    @Override
    public String toString() {
        return name + " - " + amount + " " + unit;
    }
}
