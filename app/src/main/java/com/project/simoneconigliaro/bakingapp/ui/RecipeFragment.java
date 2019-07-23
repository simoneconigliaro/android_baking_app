package com.project.simoneconigliaro.bakingapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.project.simoneconigliaro.bakingapp.R;
import com.project.simoneconigliaro.bakingapp.Widget.BakingWidgetProvider;
import com.project.simoneconigliaro.bakingapp.models.Ingredient;
import com.project.simoneconigliaro.bakingapp.models.Recipe;
import com.project.simoneconigliaro.bakingapp.models.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simone on 10/12/2017.
 */

public class RecipeFragment extends Fragment implements StepAdapter.stepAdapterOnClickHandler {
    @BindView(R.id.rv_list_ingredients)
    RecyclerView mIngredientRecyclerView;
    @BindView(R.id.rv_list_steps)
    RecyclerView mStepRecyclerView;
    IngredientAdapter mIngredientAdapter;
    StepAdapter mStepAdapter;

    public static final String ARRAYLIST_STEPS_KEY = "steps";
    public static final String POSITION_KEY = "adapter_position";
    public static final String SHARED_PREFS_INGREDIENTS_KEY = "SHARED_PREFS_INGREDIENTS_KEY";
    public static final String SHARED_PREFS_NAME_RECIPE_KEY = "SHARED_PREFS_NAME_RECIPE_KEY";

    public RecipeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intentThatStartedActivity = getActivity().getIntent();
        if (intentThatStartedActivity != null) {

                Recipe recipe = intentThatStartedActivity.getParcelableExtra(MainActivity.RECIPE_OBJECT_KEY);
                initIngredients(recipe.getIngredients());
                initSteps(recipe.getSteps());

                sendBroadcastWidget(recipe);
            }
    }

    public void sendBroadcastWidget(Recipe recipe){

        Gson gson = new Gson();
        String jsonIngredients = gson.toJson(recipe.getIngredients());
        String recipeName = recipe.getName();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SHARED_PREFS_INGREDIENTS_KEY, jsonIngredients).commit();
        editor.putString(SHARED_PREFS_NAME_RECIPE_KEY, recipeName).commit();

        Intent intent = new Intent(this.getContext(), BakingWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE\"");
        getActivity().sendBroadcast(intent);

    }

    public void initIngredients(ArrayList<Ingredient> ingredients){
        mIngredientRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mIngredientRecyclerView.setLayoutManager(layoutManager);
        mIngredientRecyclerView.setNestedScrollingEnabled(false);
        mIngredientAdapter = new IngredientAdapter(ingredients);
        mIngredientRecyclerView.setAdapter(mIngredientAdapter);
    }

    public void initSteps(ArrayList<Step> steps){
        mStepRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        mStepRecyclerView.setLayoutManager(layoutManager1);
        mStepRecyclerView.setNestedScrollingEnabled(false);
        mStepAdapter = new StepAdapter(this);
        mStepAdapter.setStepData(steps);
        mStepRecyclerView.setAdapter(mStepAdapter);
    }

    @Override
    public void onListItemClick(ArrayList<Step> steps, int position) {

        if (RecipeActivity.mTwoPane) {

            StepFragment stepFragment = new StepFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList(ARRAYLIST_STEPS_KEY, steps);
            args.putInt(POSITION_KEY, position);
            stepFragment.setArguments(args);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_fragment_tablet, stepFragment)
                    .commit();

        } else {

            Intent intent = new Intent(getContext(), StepActivity.class);
            intent.putParcelableArrayListExtra(ARRAYLIST_STEPS_KEY, steps);
            intent.putExtra(POSITION_KEY, position);
            startActivity(intent);

        }
    }
}
