package com.hannabennett.cookbook;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by HannaBennett on 11/7/17.
 */

public class Recipe {
    private UUID mId;
    private String mTitle;
    private List<Ingredient> mIngredients;
    private float mRating;

    public Recipe() {
        this(UUID.randomUUID());
        mIngredients = new ArrayList<>();
    }

    public Recipe(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public float getRating() {
        return mRating;
    }

    public void setRating(float rating) {
        mRating = rating;
    }

    public void addIngredient(Ingredient ingredient) {
        mIngredients.add(ingredient);
    }

    public String getPhotoFileName() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
