package com.project.simoneconigliaro.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.simoneconigliaro.bakingapp.R;
import com.project.simoneconigliaro.bakingapp.Widget.BakingWidgetProvider;
import com.project.simoneconigliaro.bakingapp.idlingResource.SimpleIdlingResource;
import com.project.simoneconigliaro.bakingapp.models.Ingredient;
import com.project.simoneconigliaro.bakingapp.models.Recipe;
import com.project.simoneconigliaro.bakingapp.models.Step;
import com.project.simoneconigliaro.bakingapp.utilities.RequestInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.recipeAdapterOnClickHandler{
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    public static final String RECIPE_OBJECT_KEY = "recipe";
    public static final List<String> cakePictures = Arrays.asList(new String[]{
            "android.resource://com.project.simoneconigliaro.bakingapp/drawable/nutellapie",
            "android.resource://com.project.simoneconigliaro.bakingapp/drawable/brownie",
            "android.resource://com.project.simoneconigliaro.bakingapp/drawable/yellowcake",
            "android.resource://com.project.simoneconigliaro.bakingapp/drawable/cheesecake"});

    @BindView(R.id.rv_list_recipes)
    RecyclerView mRecyclerView;
    RecipeAdapter mRecipeAdapter;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;




    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViews();
        getIdlingResource();

        Intent intent = new Intent(this, BakingWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE\"");
        sendBroadcast(intent);
    }

    private void initViews(){
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecipeAdapter = new RecipeAdapter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkConnection();
    }

    private void checkConnection(){

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            loadJSON();
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        } else {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mErrorMessageDisplay.setText(R.string.no_internet);
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mErrorMessageDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkConnection();
                }
            });
        }
    }

    private void loadJSON(){

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<ArrayList<Recipe>> call = request.getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                ArrayList<Recipe> recipes = response.body();

                for (int i = 0; i < recipes.size(); i++) {
                    if (recipes.get(i).getImage().isEmpty()){
                        recipes.get(i).setImage(cakePictures.get(i));
                    }
                }

                mRecipeAdapter.setRecipeData(recipes);
                mRecyclerView.setAdapter(mRecipeAdapter);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mErrorMessageDisplay.setVisibility(View.INVISIBLE);

                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.d("Error", t.getMessage());
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mErrorMessageDisplay.setText(R.string.error_message);
                mErrorMessageDisplay.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onListItemClick(Recipe currentRecipeClicked) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RECIPE_OBJECT_KEY, currentRecipeClicked);
        startActivity(intent);
    }
}
