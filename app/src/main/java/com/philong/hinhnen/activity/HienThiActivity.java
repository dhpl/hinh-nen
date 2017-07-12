package com.philong.hinhnen.activity;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.philong.hinhnen.R;
import com.philong.hinhnen.database.HinhDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class HienThiActivity extends AppCompatActivity {

    private HinhDatabase db;
    private ImageView imgHienThi;
    private TextView txtTaiHinh;
    private DownloadManager mDownloadManager;
    private long downloadReference;
    private boolean check;

    public static Intent newIntent(Context context, String hinh, String id, String width, String height){
        Intent intent = new Intent(context, HienThiActivity.class);
        intent.putExtra("Hinh", hinh);
        intent.putExtra("Id", id);
        intent.putExtra("Width", width);
        intent.putExtra("Height", height);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        registerBroadcast();
        setContentView(R.layout.activity_hien_thi);
        //Database
        db = new HinhDatabase(this);

        imgHienThi = (ImageView) findViewById(R.id.imgHienThi);
        txtTaiHinh = (TextView) findViewById(R.id.txtTaiXuong);
        if(getIntent() != null){
            final String hinh = getIntent().getStringExtra("Hinh");
            final String id = getIntent().getStringExtra("Id");
            int width = Integer.parseInt(getIntent().getStringExtra("Width"));
            int height = Integer.parseInt(getIntent().getStringExtra("Height"));
            Picasso.with(this).load(hinh)
                    .error(R.drawable.placeholder)
                    .resize(width, height)
                    .centerInside()
                    .into(imgHienThi);
            check = db.kiemTraHinhTonTai(id);
            if(check){
                txtTaiHinh.setText("Áp dụng");
            }else{
                txtTaiHinh.setText("Tải xuống");
            }
            txtTaiHinh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(check){
                        String path = db.getPath(id);
                        apDung(path);
                        Toast.makeText(HienThiActivity.this, "Áp dụng thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        taiHinh(hinh, id);
                        db.themHinh(id, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + id + ".jpg", hinh);
                        check = true;
                    }
                }
            });
        }
    }

    private long taiHinh(String link, String id){
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));
        request.setTitle("Đang tải");
        request.setDescription("Vui lòng chờ...");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/" + id + ".jpg");
        downloadReference = mDownloadManager.enqueue(request);
        return downloadReference;
    }

    private void apDung(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        WallpaperManager wm = WallpaperManager.getInstance(this);
        try {
            wm.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if(reference == downloadReference ){
                txtTaiHinh.setText("Áp dụng");
            }
        }
    };

    private void registerBroadcast(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
