package com.philong.hinhnen.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 7/1/2017.
 */

public class Photo {

    @SerializedName("id")
    private String mId;
    @SerializedName("owner")
    private String mOwner;
    @SerializedName("secret")
    private String mSecret;
    @SerializedName("server")
    private String mServer;
    @SerializedName("farm")
    private int mFarm;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("ispublic")
    private int mIsPublic;
    @SerializedName("isfriend")
    private int mIsFriend;
    @SerializedName("isfamily")
    private int mIsFamily;
    @SerializedName("url_c")
    private String mUrl_m;
    @SerializedName("height_c")
    private String mHeight_m;
    @SerializedName("width_c")
    private String mWidth_m;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        mOwner = owner;
    }

    public String getSecret() {
        return mSecret;
    }

    public void setSecret(String secret) {
        mSecret = secret;
    }

    public String getServer() {
        return mServer;
    }

    public void setServer(String server) {
        mServer = server;
    }

    public int getFarm() {
        return mFarm;
    }

    public void setFarm(int farm) {
        mFarm = farm;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getIsPublic() {
        return mIsPublic;
    }

    public void setIsPublic(int isPublic) {
        mIsPublic = isPublic;
    }

    public int getIsFriend() {
        return mIsFriend;
    }

    public void setIsFriend(int isFriend) {
        mIsFriend = isFriend;
    }

    public int getIsFamily() {
        return mIsFamily;
    }

    public void setIsFamily(int isFamily) {
        mIsFamily = isFamily;
    }

    public String getUrl_m() {
        return mUrl_m;
    }

    public void setUrl_m(String url_m) {
        mUrl_m = url_m;
    }

    public String getHeight_m() {
        return mHeight_m;
    }

    public void setHeight_m(String height_m) {
        mHeight_m = height_m;
    }

    public String getWidth_m() {
        return mWidth_m;
    }

    public void setWidth_m(String width_m) {
        mWidth_m = width_m;
    }
}
