package com.project.simoneconigliaro.bakingapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.simoneconigliaro.bakingapp.R;
import com.project.simoneconigliaro.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simone on 06/12/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{
    private ArrayList<Recipe> mRecipes;
    recipeAdapterOnClickHandler mOnClickHandler;

    public RecipeAdapter(recipeAdapterOnClickHandler onClickHandler){
        this.mOnClickHandler = onClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.nameRecipeTextView.setText(mRecipes.get(position).getName());
        Picasso.with(viewHolder.imageView.getContext()).load(mRecipes.get(position).getImage()).error(R.drawable.placeholder_error).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    interface recipeAdapterOnClickHandler{
        void onListItemClick(Recipe currentRecipeClicked);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tv_name_recipe)
        TextView nameRecipeTextView;
        @BindView(R.id.image_view)
        ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe currentRecipeClicked = mRecipes.get(adapterPosition);
            mOnClickHandler.onListItemClick(currentRecipeClicked);
        }
    }

    public void setRecipeData(ArrayList<Recipe> recipes){
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }



}
