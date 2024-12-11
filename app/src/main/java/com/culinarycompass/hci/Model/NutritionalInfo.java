package com.culinarycompass.hci.Model;

public class NutritionalInfo {
    private int Calories;
    private int Protein;
    private int Carbohydrates;
    private int Fat;

    public NutritionalInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(NutritionalInfo.class)
    }

    // Getters and Setters
    public int getCalories() {
        return Calories;
    }

    public void setCalories(int calories) {
        Calories = calories;
    }

    public int getProtein() {
        return Protein;
    }

    public void setProtein(int protein) {
        Protein = protein;
    }

    public int getCarbohydrates() {
        return Carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        Carbohydrates = carbohydrates;
    }

    public int getFat() {
        return Fat;
    }

    public void setFat(int fat) {
        Fat = fat;
    }
}
