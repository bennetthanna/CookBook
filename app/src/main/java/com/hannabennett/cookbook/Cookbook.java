package com.hannabennett.cookbook;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by HannaBennett on 11/7/17.
 */

public class Cookbook {
    private static Cookbook sCookbook;
    private List<Recipe> mRecipes;

    public static Cookbook getInstance(Context context) {
        if (sCookbook == null) {
            sCookbook = new Cookbook(context);
        }
        return sCookbook;
    }

    private Cookbook(Context context) {
        mRecipes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle("Recipe #" + i);
            mRecipes.add(recipe);
        }
    }

    public void addRecipe(Recipe recipe) {
        mRecipes.add(recipe);
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

}
