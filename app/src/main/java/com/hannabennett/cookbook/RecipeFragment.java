package com.hannabennett.cookbook;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static android.widget.AdapterView.*;

/**
 * Created by HannaBennett on 11/8/17.
 */

public class RecipeFragment extends Fragment {
    private static final String ARG_RECIPE_ID = "recipe_id";
    private static final int REQUEST_DECISION = 0;
    private static final int REQUEST_PHOTO = 1;
    private static final String DIALOG_DELETE_RECIPE = "DialogDeleteRecipe";

    private Recipe mRecipe;
    private File mPhotoFile;
    private LinearLayout mLayout;
    private EditText mTitleField;
    private EditText mIngredientField;
    private RatingBar mRatingBar;
    private Button mAddIngredientButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Spinner mIngredientMeasurementSpinner;
    private Spinner mIngredientQuantitySpinner;
    private String mQuantity;
    private String mMeasurement;

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
        setHasOptionsMenu(true);
        UUID recipeId = (UUID) getArguments().getSerializable(ARG_RECIPE_ID);
        mRecipe = Cookbook.getInstance(getActivity()).getRecipe(recipeId);
        mPhotoFile = Cookbook.getInstance(getActivity()).getPhotoFile(mRecipe);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_recipe, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_recipe:
                FragmentManager manager = getFragmentManager();
                DeleteRecipeFragment dialog = new DeleteRecipeFragment();
                dialog.setTargetFragment(RecipeFragment.this, REQUEST_DECISION);
                dialog.show(manager, DIALOG_DELETE_RECIPE);
                return true;
            case R.id.share_recipe:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getRecipeSummary());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.recipe_summary_subject));
                i = Intent.createChooser(i, getString(R.string.send_recipe));
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getRecipeSummary() {
        String recipeTitle = mRecipe.getTitle();

        List<String> recipeIngredients = new ArrayList<>();
        for (Ingredient i : mRecipe.getIngredients()) {
            String ingredient = i.getQuantity() + " " + i.getMeasurement() +
                    " " + i.getItem();
            recipeIngredients.add(ingredient);
        }

        float recipeRating = mRecipe.getRating();

        String recipeSummary = getString(R.string.recipe_summary, recipeTitle,
                Arrays.toString(recipeIngredients.toArray(new String[0])), recipeRating);
        return recipeSummary;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DECISION) {
            Cookbook cookbook = Cookbook.getInstance(getActivity());
            Recipe recipe = cookbook.getRecipe(mRecipe.getId());
            cookbook.deleteRecipe(recipe);
            getActivity().finish();
        }
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
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.hannabennett.cookbook.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) view.findViewById(R.id.recipe_photo);

        mRatingBar = (RatingBar) view.findViewById(R.id.recipe_rating);
        mRatingBar.setRating(mRecipe.getRating());
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRecipe.setRating(v);
            }
        });

        mIngredientQuantitySpinner = (Spinner) view.findViewById(R.id.ingredient_quantity);
        mIngredientQuantitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mQuantity = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> fractionAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.fractions_array, android.R.layout.simple_spinner_item);
        fractionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mIngredientQuantitySpinner.setAdapter(fractionAdapter);

        mIngredientMeasurementSpinner = (Spinner) view.findViewById(R.id.ingredient_measurement);
        mIngredientMeasurementSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mMeasurement = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> measurementAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.measurement_array, android.R.layout.simple_spinner_item);
        measurementAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mIngredientMeasurementSpinner.setAdapter(measurementAdapter);

        mLayout = (LinearLayout) view.findViewById(R.id.layout);
        if (mRecipe.getIngredients() != null) {
            for (Ingredient ingredient : mRecipe.getIngredients()) {
                mLayout.addView(createIngredientTextView(ingredient));
            }
        }

        mIngredientField = (EditText) view.findViewById(R.id.recipe_ingredient_edit_text);
        mAddIngredientButton = (Button) view.findViewById(R.id.recipe_add_ingredient);
        mAddIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIngredientField.getText().toString().equals("")) {
                    String emptyIngredientToast = getString(R.string.empty_ingredient);
                    Toast.makeText(getActivity(), emptyIngredientToast, Toast.LENGTH_SHORT).show();
                } else {
                    Ingredient i = new Ingredient(mIngredientField.getText().toString(), mQuantity, mMeasurement);
                    mRecipe.addIngredient(i);
                    mLayout.addView(createIngredientTextView(i));
                    mIngredientField.setText("");
                }
            }
        });
        return view;
    }

    private RelativeLayout createIngredientTextView(Ingredient i) {
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final RelativeLayout relativeLayout = new RelativeLayout(getActivity());
        relativeLayout.setLayoutParams(params);
        final Button editButton = new Button(getActivity());
        final RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        editButton.setLayoutParams(buttonParams);
        editButton.setText(R.string.edit);
        editButton.setId(1);
        relativeLayout.addView(editButton);
        final Button deleteButton = new Button(getActivity());
        final RelativeLayout.LayoutParams deleteButtonParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        deleteButtonParams.addRule(RelativeLayout.LEFT_OF, 1);
        deleteButton.setLayoutParams(deleteButtonParams);
        deleteButton.setText(R.string.delete);
        relativeLayout.addView(deleteButton);
        final TextView textView = new TextView(getActivity());
        textView.setText(i.getQuantity() + "  " + i.getMeasurement() + "  " + i.getItem());
        relativeLayout.addView(textView);
        return relativeLayout;
    }

}
