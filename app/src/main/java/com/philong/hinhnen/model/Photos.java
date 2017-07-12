package com.philong.hinhnen.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Long on 7/1/2017.
 */

public class Photos {

    @SerializedName("page")
    private int mPage;
    @SerializedName("pages")
    private int mPages;
    @SerializedName("perpage")
    private int mPerPage;
    @SerializedName("total")
    private int mTotal;
    @SerializedName("photo")
    private List<Photo> mPhoto;

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public int getPages() {
        return mPages;
    }

    public void setPages(int pages) {
        mPages = pages;
    }

    public int getPerPage() {
        return mPerPage;
    }

    public void setPerPage(int perPage) {
        mPerPage = perPage;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public List<Photo> getPhoto() {
        return mPhoto;
    }

    public void setPhoto(List<Photo> photo) {
        mPhoto = photo;
    }
}
