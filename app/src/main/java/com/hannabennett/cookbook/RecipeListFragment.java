package com.hannabennett.cookbook;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecipeListFragment extends Fragment {

    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_recipe_list, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        mRecipeRecyclerView = (RecyclerView) view.findViewById(R.id.recipe_recycler_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.new_recipe:
                Recipe recipe = new Recipe();
                Cookbook.getInstance(getActivity()).addRecipe(recipe);
                Intent intent = RecipePagerActivity.newIntent(getActivity(), recipe.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        Cookbook cookbook = Cookbook.getInstance(getActivity());
        int numRecipes = cookbook.getNumRecipes();
        String subtitle = getString(R.string.subtitle_format, numRecipes);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private Recipe mRecipe;

        public RecipeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_recipe, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.recipe_title);
        }

        public void bind(Recipe recipe) {
            mRecipe = recipe;
            mTitleTextView.setText(mRecipe.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = RecipePagerActivity.newIntent(getActivity(), mRecipe.getId());
            startActivity(intent);
        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {
        private List<Recipe> mRecipes;

        public RecipeAdapter(List<Recipe> recipes) {
            mRecipes = recipes;
        }

        @Override
        public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new RecipeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(RecipeHolder holder, int position) {
            Recipe recipe = mRecipes.get(position);
            holder.bind(recipe);
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }
    }

    private void updateUI() {
        Cookbook cookbook = Cookbook.getInstance(getActivity());
        List<Recipe> recipes = cookbook.getRecipes();

        mAdapter = new RecipeAdapter(recipes);
        mRecipeRecyclerView.setAdapter(mAdapter);
        updateSubtitle();
    }
}
