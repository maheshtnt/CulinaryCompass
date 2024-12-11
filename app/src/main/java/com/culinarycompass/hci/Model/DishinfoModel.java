package com.culinarycompass.hci.Model;

import java.util.ArrayList;

public class DishinfoModel {

    private int id;
    private int dish_ref;
    private String name;
    private ArrayList<String> image;

    private String description;
    private String ingredients;
    private boolean diabetic_rec;
    private String note;
    private NutritionalInfo nutritional_info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDish_ref() {
        return dish_ref;
    }

    public void setDish_ref(int dish_ref) {
        this.dish_ref = dish_ref;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public boolean isDiabetic_rec() {
        return diabetic_rec;
    }

    public void setDiabetic_rec(boolean diabetic_rec) {
        this.diabetic_rec = diabetic_rec;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public NutritionalInfo getNutritional_info() {
        return nutritional_info;
    }

    public void setNutritional_info(NutritionalInfo nutritional_info) {
        this.nutritional_info = nutritional_info;
    }

    public DishinfoModel() {
    }

    public DishinfoModel(int id, int dish_ref, String name, ArrayList<String> image, String description, String ingredients, boolean diabetic_rec, String note, NutritionalInfo nutritional_info) {
        this.id = id;
        this.dish_ref = dish_ref;
        this.name = name;
        this.image = image;
        this.description = description;
        this.ingredients = ingredients;
        this.diabetic_rec = diabetic_rec;
        this.note = note;
        this.nutritional_info = nutritional_info;
    }
}
