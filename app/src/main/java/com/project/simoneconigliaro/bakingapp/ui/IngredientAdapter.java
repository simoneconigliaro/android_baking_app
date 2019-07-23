package com.project.simoneconigliaro.bakingapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.simoneconigliaro.bakingapp.R;
import com.project.simoneconigliaro.bakingapp.models.Ingredient;
import com.project.simoneconigliaro.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simone on 10/12/2017.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private ArrayList<Ingredient> mIngredients;



    public IngredientAdapter(ArrayList<Ingredient> ingredients) {
        this.mIngredients = ingredients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.quantityTextView.setText(mIngredients.get(position).getQuantity());
        holder.measureTextView.setText(mIngredients.get(position).getMeasure());
        holder.ingredientTextView.setText(mIngredients.get(position).getIngredient());
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_quantity)
        TextView quantityTextView;
        @BindView(R.id.tv_measure)
        TextView measureTextView;
        @BindView(R.id.tv_ingredient)
        TextView ingredientTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
