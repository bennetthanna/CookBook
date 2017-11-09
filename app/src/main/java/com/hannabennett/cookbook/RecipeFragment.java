package com.hannabennett.cookbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by HannaBennett on 11/8/17.
 */

public class RecipeFragment extends Fragment {
    private static final String ARG_RECIPE_ID = "recipe_id";

    private Recipe mRecipe;
    private LinearLayout mLayout;
    private EditText mTitleField;
    private EditText mIngredientField;
    private RatingBar mRatingBar;
    private Button mAddIngredientButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    public static RecipeFragment newInstance(UUID recipeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE_ID, recipeId);

        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID recipeId = (UUID) getArguments().getSerializable(ARG_RECIPE_ID);
        mRecipe = Cookbook.getInstance(getActivity()).getRecipe(recipeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        mTitleField = (EditText) view.findViewById(R.id.recipe_title);
        mTitleField.setText(mRecipe.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRecipe.setTitle(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPhotoButton = (ImageButton) view.findViewById(R.id.recipe_camera);
        mPhotoView = (ImageView) view.findViewById(R.id.recipe_photo);

        mLayout = (LinearLayout) view.findViewById(R.id.layout);
        if (mRecipe.getIngredients() != null) {
            for (String ingredient : mRecipe.getIngredients()) {
                mLayout.addView(createNewTextView(ingredient));
            }
        }
        mIngredientField = (EditText) view.findViewById(R.id.recipe_ingredient_edit_text);
        mAddIngredientButton = (Button) view.findViewById(R.id.recipe_add_ingredient);
        mAddIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIngredientField.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Enter an ingredient", Toast.LENGTH_SHORT).show();
                } else {
                    mLayout.addView(createNewTextView(mIngredientField.getText().toString()));
                    mRecipe.addIngredient(mIngredientField.getText().toString());
                    mIngredientField.setText("");
                }
            }
        });
        return view;
    }

    private TextView createNewTextView(String text) {
        final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(getActivity());
        textView.setLayoutParams(params);
        textView.setText(text);
        return textView;
    }
}
