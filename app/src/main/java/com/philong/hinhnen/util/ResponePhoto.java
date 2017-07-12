package com.philong.hinhnen.util;

import com.philong.hinhnen.model.Flickr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Long on 7/1/2017.
 */

public interface ResponePhoto {

    @GET("services/rest")
    Call<Flickr> getAllPhoto(@Query("method") String method, @Query("api_key") String apiKey, @Query("format") String format, @Query("nojsoncallback") int jsonCallBack, @Query("extras") String extra, @Query("page") int page, @Query("per_page") int perPage);

    @GET("services/rest")
    Call<Flickr> getPhotoSearch(@Query("method") String method, @Query("api_key") String apiKey, @Query("text") String text, @Query("safe_search") int safe,
                                @Query("extras") String extras, @Query("page") int page, @Query("per_page") int perPage, @Query("format") String format,
                                @Query("nojsoncallback") int noJsonCallBack);


}
