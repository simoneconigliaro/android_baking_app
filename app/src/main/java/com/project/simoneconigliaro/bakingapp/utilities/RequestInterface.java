package com.project.simoneconigliaro.bakingapp.utilities;

import com.project.simoneconigliaro.bakingapp.models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Simone on 06/12/2017.
 */

public interface RequestInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
