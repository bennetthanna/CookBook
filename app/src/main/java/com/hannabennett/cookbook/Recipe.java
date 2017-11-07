package com.hannabennett.cookbook;

import java.util.UUID;

/**
 * Created by HannaBennett on 11/7/17.
 */

public class Recipe {
    private UUID mId;
    private String mTitle;
    private String[] mIngredients;
    private int mRating;

    public Recipe() {
        this(UUID.randomUUID());
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

    public String[] getIngredients() {
        return mIngredients;
    }

    public void setIngredients(String[] ingredients) {
        mIngredients = ingredients;
    }

    public int getRating() {
        return mRating;
    }

    public void setRating(int rating) {
        mRating = rating;
    }
}
