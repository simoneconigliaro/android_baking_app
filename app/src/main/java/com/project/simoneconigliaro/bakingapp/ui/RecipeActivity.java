package com.project.simoneconigliaro.bakingapp.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.project.simoneconigliaro.bakingapp.R;
import com.project.simoneconigliaro.bakingapp.models.Ingredient;
import com.project.simoneconigliaro.bakingapp.models.Recipe;
import com.project.simoneconigliaro.bakingapp.models.Step;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    public static final String ARRAYLIST_STEPS_KEY = "steps";
    public static final String POSITION_KEY = "adapter_position";
    private final static String LOG_TAG = RecipeActivity.class.getSimpleName();
    public static boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intentThatStartedActivity = getIntent();
        Recipe recipe = intentThatStartedActivity.getParcelableExtra(MainActivity.RECIPE_OBJECT_KEY);
        String recipeName = recipe.getName();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(recipeName);
        }
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        if (tabletSize) {
            mTwoPane = true;

            if (savedInstanceState == null) {

                if (intentThatStartedActivity != null) {
                    ArrayList<Step> steps = recipe.getSteps();

                    RecipeFragment recipeFragment = new RecipeFragment();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.recipe_fragment_tablet, recipeFragment)
                            .commit();

                    StepFragment stepFragment = new StepFragment();
                    Bundle args = new Bundle();
                    args.putParcelableArrayList(ARRAYLIST_STEPS_KEY, steps);
                    args.putInt(POSITION_KEY, 0);
                    stepFragment.setArguments(args);

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.step_fragment_tablet, stepFragment)
                            .commit();
                }
            }
        }
    }
}
