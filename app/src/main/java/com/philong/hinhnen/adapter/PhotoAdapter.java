package com.philong.hinhnen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.philong.hinhnen.R;
import com.philong.hinhnen.activity.HienThiActivity;
import com.philong.hinhnen.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Long on 7/1/2017.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>{

    private Context mContext;
    private List<Photo> mPhotoList;

    public PhotoAdapter(Context context, List<Photo> photo) {
        mContext = context;
        mPhotoList = photo;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hinh, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        final Photo photo = mPhotoList.get(position);
        Picasso.with(mContext).load(photo.getUrl_m())
                .error(R.drawable.placeholder)
                .resize(Integer.parseInt(photo.getWidth_m()), Integer.parseInt(photo.getHeight_m()))
                .centerInside()
                .into(holder.imgHinh);
        holder.imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(HienThiActivity.newIntent(mContext, photo.getUrl_m(), photo.getId(), photo.getWidth_m(), photo.getHeight_m()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgHinh;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            imgHinh = (ImageView) itemView.findViewById(R.id.imgHinh);
        }
    }

}
