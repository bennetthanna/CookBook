package com.hannabennett.cookbook;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by HannaBennett on 11/7/17.
 */

public class Cookbook {
    private static Cookbook sCookbook;
    private List<Recipe> mRecipes;
    private Context mContext;

    public static Cookbook getInstance(Context context) {
        if (sCookbook == null) {
            sCookbook = new Cookbook(context);
        }
        return sCookbook;
    }

    private Cookbook(Context context) {
        mContext = context.getApplicationContext();
        mRecipes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle("Recipe #" + i);
            Ingredient ingredient = new Ingredient("BUTTAH", "1/2", "tsp");
            recipe.addIngredient(ingredient);
            mRecipes.add(recipe);
        }
    }

    public void addRecipe(Recipe recipe) {
        mRecipes.add(recipe);
    }

    public void deleteRecipe(Recipe recipe) {
        mRecipes.remove(recipe);
    }

    public int getNumRecipes() {
        return mRecipes.size();
    }

    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    public Recipe getRecipe(UUID id) {
        for (Recipe recipe : mRecipes) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }

    public File getPhotoFile(Recipe recipe) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, recipe.getPhotoFileName());
    }
}
