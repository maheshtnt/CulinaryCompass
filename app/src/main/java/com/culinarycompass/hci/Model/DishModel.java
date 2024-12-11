package com.culinarycompass.hci.Model;

public class DishModel {

    int id,ref,DRM;
    String dish,description,type;


    public DishModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    public int getDRM() {
        return DRM;
    }

    public void setDRM(int DRM) {
        this.DRM = DRM;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DishModel(int id, int ref, int DRM, String dish, String description, String type) {
        this.id = id;
        this.ref = ref;
        this.DRM = DRM;
        this.dish = dish;
        this.description = description;
        this.type = type;
    }


}
