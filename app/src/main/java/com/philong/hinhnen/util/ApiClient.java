package com.philong.hinhnen.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Long on 7/1/2017.
 */

public class ApiClient {

    public static final String API_KEY = "805241eb22bc177a8009d40f12a80270";
    public static final String BASE_URL = "https://api.flickr.com/";
    public static final String RECENT_PHOTOS = "flickr.photos.getRecent";
    public static final String SEARCH_PHOTOS = "flickr.photos.search";

    public static Retrofit mRetrofit = null;

    public static Retrofit getApiClient(){
        if(mRetrofit == null){
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

}
