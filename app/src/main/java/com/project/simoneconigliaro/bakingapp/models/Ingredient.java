package com.project.simoneconigliaro.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Simone on 06/12/2017.
 */

public class Ingredient implements Parcelable{

    private String quantity;
    private String measure;
    private String ingredient;

    public Ingredient() {

    }

    public String getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public Ingredient(Parcel in){
        this.quantity = in.readString();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {return new Ingredient(in);}

        @Override
        public Ingredient[] newArray(int size) {return new Ingredient[size];}
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }
}
