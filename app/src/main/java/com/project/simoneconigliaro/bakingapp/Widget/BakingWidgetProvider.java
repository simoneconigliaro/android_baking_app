package com.project.simoneconigliaro.bakingapp.Widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.simoneconigliaro.bakingapp.R;
import com.project.simoneconigliaro.bakingapp.models.Ingredient;
import com.project.simoneconigliaro.bakingapp.ui.RecipeFragment;

import java.security.Provider;
import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeName = preferences.getString(RecipeFragment.SHARED_PREFS_NAME_RECIPE_KEY, "");
        if (recipeName.isEmpty()){
            recipeName = "Select a recipe";
            views.setViewVisibility(R.id.widget_tv_select_recipe, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.widget_tv_select_recipe, View.GONE);
        }

        views.setTextViewText(R.id.widget_tv_recipe_name, recipeName);
        views.setRemoteAdapter(R.id.widget_list_ingredients, new Intent(context, WidgetService.class));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_ingredients);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), BakingWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}

