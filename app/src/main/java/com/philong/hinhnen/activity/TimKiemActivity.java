package com.philong.hinhnen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.philong.hinhnen.R;
import com.philong.hinhnen.adapter.PhotoAdapter;
import com.philong.hinhnen.model.Flickr;
import com.philong.hinhnen.model.Photo;
import com.philong.hinhnen.util.ApiClient;
import com.philong.hinhnen.util.ResponePhoto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimKiemActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    //recyclerview
    private RecyclerView mRecyclerViewTimKiem;
    private PhotoAdapter mPhotoAdapterTimKiem;
    private List<Photo> mPhotoListTimKiem;
    private GridLayoutManager mGridLayoutManagerTimKiem;
    private ResponePhoto mResponePhotoTimKiem;
    private Call<Flickr> mFlickrCallTimKiem;
    private String query;
    //phan trang
    private int page = 1;
    private final int PER_PAGE = 20;
    private boolean isLastPage = false;
    private int visibleHold = 5;

    public static Intent newIntent(Context context, String timKiem){
        Intent intent  = new Intent(context, TimKiemActivity.class);
        intent.putExtra("TimKiem", timKiem);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem);
        if(getIntent() != null){
            query = getIntent().getStringExtra("TimKiem");
        }
        //setup toolbar;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(getSupportActionBar() == null){
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("Tìm kiếm");
        }
        //setup recyclerview
        mPhotoListTimKiem = new ArrayList<>();
        mRecyclerViewTimKiem = (RecyclerView) findViewById(R.id.recyclerViewTimKiem);
        mRecyclerViewTimKiem.setHasFixedSize(true);
        mRecyclerViewTimKiem.setNestedScrollingEnabled(false);
        mRecyclerViewTimKiem.setItemAnimator(new DefaultItemAnimator());
        mGridLayoutManagerTimKiem = new GridLayoutManager(this, 2);
        mRecyclerViewTimKiem.setLayoutManager(mGridLayoutManagerTimKiem);
        mRecyclerViewTimKiem.addOnScrollListener(reOnScrollListener);
        mResponePhotoTimKiem = ApiClient.getApiClient().create(ResponePhoto.class);
        mFlickrCallTimKiem = mResponePhotoTimKiem.getPhotoSearch(ApiClient.SEARCH_PHOTOS, ApiClient.API_KEY, query,
                1, "url_c", page, PER_PAGE, "json", 1);
        mFlickrCallTimKiem.enqueue(new Callback<Flickr>() {
            @Override
            public void onResponse(Call<Flickr> call, Response<Flickr> response) {
                Flickr flickr =  response.body();
                for(Photo photo : flickr.getPhotos().getPhoto()){
                    if(photo.getUrl_m() == null){
                        continue;
                    }
                    mPhotoListTimKiem.add(photo);
                }
                mPhotoAdapterTimKiem = new PhotoAdapter(TimKiemActivity.this, mPhotoListTimKiem);
                mRecyclerViewTimKiem.setAdapter(mPhotoAdapterTimKiem);
            }

            @Override
            public void onFailure(Call<Flickr> call, Throwable t) {

            }
        });
    }

    private RecyclerView.OnScrollListener reOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int first = mGridLayoutManagerTimKiem.findFirstVisibleItemPosition();
            int visible = mGridLayoutManagerTimKiem.getChildCount();
            int total = mGridLayoutManagerTimKiem.getItemCount();
            if (!isLastPage) {
                if((total - visible) <= (first + visibleHold)){
                    page+=1;
                    loadMore(page);
                }
            }
        }
    };

    private void loadMore(int page){
        mFlickrCallTimKiem = mResponePhotoTimKiem.getPhotoSearch(ApiClient.SEARCH_PHOTOS, ApiClient.API_KEY, query,
                1, "url_c", page, PER_PAGE, "json", 1);
        mFlickrCallTimKiem.enqueue(new Callback<Flickr>() {
            @Override
            public void onResponse(Call<Flickr> call, Response<Flickr> response) {
                Flickr flickr =  response.body();
                if(flickr.getPhotos().getPhoto().size() < PER_PAGE){
                    isLastPage = true;
                }
                for(Photo photo : flickr.getPhotos().getPhoto()){
                    if(photo.getUrl_m() == null){
                        continue;
                    }
                    mPhotoListTimKiem.add(photo);
                }
                mPhotoAdapterTimKiem.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Flickr> call, Throwable t) {

            }
        });
    }
}
