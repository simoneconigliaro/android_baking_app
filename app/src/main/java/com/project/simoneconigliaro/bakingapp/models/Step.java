package com.project.simoneconigliaro.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Simone on 06/12/2017.
 */

public class Step implements Parcelable{

    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step(){

    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() { return  description; }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {return thumbnailURL; }

    public Step(Parcel in){
        this.id = in.readInt();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {return new Step(in);}

        @Override
        public Step[] newArray(int size) {return new Step[size];}
    };

    @Override
    public int describeContents() {return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }
}
