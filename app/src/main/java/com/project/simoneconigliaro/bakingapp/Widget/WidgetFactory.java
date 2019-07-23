package com.project.simoneconigliaro.bakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.simoneconigliaro.bakingapp.models.Ingredient;
import com.project.simoneconigliaro.bakingapp.ui.RecipeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simone on 29/12/2017.
 */

public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory{

    List<Ingredient> ingredients;
    Context context;

    public WidgetFactory(Context context) {
        this.context = context;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonIngredients = preferences.getString(RecipeFragment.SHARED_PREFS_INGREDIENTS_KEY, "");
        if (!jsonIngredients.equals("")) {
            Gson gson = new Gson();
            ingredients = gson.fromJson(jsonIngredients, new TypeToken<ArrayList<Ingredient>>() {
            }.getType());
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredients != null) {
            return ingredients.size();
        } else return 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), android.R.layout.simple_list_item_1);
        if (ingredients != null) {
            String quantity = ingredients.get(i).getQuantity();
            String measure = ingredients.get(i).getMeasure();
            String ingredient = ingredients.get(i).getIngredient();
            remoteView.setTextViewText(android.R.id.text1, quantity + " " + measure + " " + ingredient);
        }
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
