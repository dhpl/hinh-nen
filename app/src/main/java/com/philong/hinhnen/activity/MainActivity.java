package com.philong.hinhnen.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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

public class MainActivity extends AppCompatActivity {

    //toolbar
    private Toolbar mToolbar;
    //swipe
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //Photo
    private RecyclerView mRecyclerView;
    private PhotoAdapter mPhotoAdapter;
    private List<Photo> mPhotos;
    private GridLayoutManager mGridLayoutManager;
    private ResponePhoto mResponePhoto;
    private Call<Flickr> mCall;
    //phan trang
    private boolean isLastPage = false;
    private int page = 1;
    private static final int PER_PAGE = 20;
    private int visibleThreshold = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setup toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(getSupportActionBar() == null){
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(R.string.app_name);
        }

        //setup recyclerview
        mPhotos = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addOnScrollListener(reOnScrollListener);
        mRecyclerView.setNestedScrollingEnabled(false);
        mResponePhoto = ApiClient.getApiClient().create(ResponePhoto.class);
        mCall = mResponePhoto.getAllPhoto(ApiClient.RECENT_PHOTOS, ApiClient.API_KEY, "json", 1, "url_c", page, PER_PAGE);
        mCall.enqueue(new Callback<Flickr>() {
            @Override
            public void onResponse(Call<Flickr> call, Response<Flickr> response) {
                Flickr flickr = response.body();
                for(Photo photo : flickr.getPhotos().getPhoto()){
                    if(photo.getUrl_m() == null){
                        continue;
                    }
                    mPhotos.add(photo);
                }
                mPhotoAdapter = new PhotoAdapter(MainActivity.this, mPhotos);
                mRecyclerView.setAdapter(mPhotoAdapter);
            }

            @Override
            public void onFailure(Call<Flickr> call, Throwable t) {

            }
        });
        //swipe
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isLastPage = false;
                mCall = mResponePhoto.getAllPhoto(ApiClient.RECENT_PHOTOS, ApiClient.API_KEY, "json", 1, "url_c", page, PER_PAGE);
                mCall.enqueue(new Callback<Flickr>() {
                    @Override
                    public void onResponse(Call<Flickr> call, Response<Flickr> response) {
                        Flickr flickr = response.body();
                        if(!mPhotos.isEmpty()){
                            mPhotos.clear();
                        }
                        for(Photo photo : flickr.getPhotos().getPhoto()){
                            if(photo.getUrl_m() == null){
                                continue;
                            }
                            mPhotos.add(photo);
                        }
                        mPhotoAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<Flickr> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startActivity(TimKiemActivity.newIntent(MainActivity.this, query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private RecyclerView.OnScrollListener reOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int first = mGridLayoutManager.findFirstVisibleItemPosition();
            if(first > 1){
                mSwipeRefreshLayout.setEnabled(false);
            }else if(first == 0){
                mSwipeRefreshLayout.setEnabled(true);
                if(mRecyclerView.getScrollState() == 1){
                    if(mSwipeRefreshLayout.isRefreshing()){
                        mRecyclerView.stopScroll();
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visible = mGridLayoutManager.getChildCount();
            int total = mGridLayoutManager.getItemCount();
            int first = mGridLayoutManager.findFirstVisibleItemPosition();
            if(!isLastPage){
                if((total - visible) <= (first + visibleThreshold)){
                    page+=1;
                    loadMore(page);
                }
            }

        }
    };

    private void loadMore(int page){
        mCall = mResponePhoto.getAllPhoto(ApiClient.RECENT_PHOTOS, ApiClient.API_KEY, "json", 1, "url_c", page, PER_PAGE);
        mCall.enqueue(new Callback<Flickr>() {
            @Override
            public void onResponse(Call<Flickr> call, Response<Flickr> response) {
                Flickr flickr = response.body();
                if(flickr.getPhotos().getPhoto().size() < PER_PAGE){
                    isLastPage = true;
                }
                for(Photo photo : flickr.getPhotos().getPhoto()){
                    if(photo.getUrl_m() == null){
                        continue;
                    }
                    mPhotos.add(photo);
                }
                mPhotoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Flickr> call, Throwable t) {

            }
        });
    }

}
