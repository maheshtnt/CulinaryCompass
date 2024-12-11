package com.culinarycompass.hci.Model;

public class RestNearModel {

    int id, nratings;
    float distance, rating;
    String image, title, cuisine;
    boolean DiabeticFriendly;

    public RestNearModel() {

    }

    public RestNearModel(int id, int nratings, float distance, float rating, String image, String title, String cuisine, boolean diabeticFriendly) {
        this.id = id;
        this.nratings = nratings;
        this.distance = distance;
        this.rating = rating;
        this.image = image;
        this.title = title;
        this.cuisine = cuisine;
        DiabeticFriendly = diabeticFriendly;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNratings() {
        return nratings;
    }

    public void setNratings(int nratings) {
        this.nratings = nratings;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public boolean isDiabeticFriendly() {
        return DiabeticFriendly;
    }

    public void setDiabeticFriendly(boolean diabeticFriendly) {
        DiabeticFriendly = diabeticFriendly;
    }
}
