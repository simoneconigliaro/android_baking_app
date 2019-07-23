package com.project.simoneconigliaro.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simone on 06/12/2017.
 */

public class Recipe implements Parcelable{

    private String name;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<Step> steps = new ArrayList<>();
    private String servings;
    private String image;

    public Recipe () {

    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public String getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Recipe (Parcel in){
        this.name = in.readString();

        in.readTypedList(ingredients, Ingredient.CREATOR);

        in.readTypedList(steps, Step.CREATOR);

        this.servings = in.readString();

        this.image = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {return new Recipe(in);}

        @Override
        public Recipe[] newArray(int size) {return new Recipe[size];}
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
        parcel.writeString(servings);
        parcel.writeString(image);
    }
}
