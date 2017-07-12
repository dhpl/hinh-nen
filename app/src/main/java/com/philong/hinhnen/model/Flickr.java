package com.philong.hinhnen.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 7/1/2017.
 */

public class Flickr {

    @SerializedName("photos")
    private Photos mPhotos;
    @SerializedName("stat")
    private String mStat;

    public Photos getPhotos() {
        return mPhotos;
    }

    public void setPhotos(Photos photos) {
        mPhotos = photos;
    }

    public String getStat() {
        return mStat;
    }

    public void setStat(String stat) {
        mStat = stat;
    }
}
