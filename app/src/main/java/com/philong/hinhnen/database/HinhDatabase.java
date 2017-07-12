package com.philong.hinhnen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Long on 7/1/2017.
 */

public class HinhDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbhinh.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_HINH = "Hinh";
    private static final String HINH_ID_AUTO = "id";
    private static final String HINH_ID = "idHinh";
    private static final String HINH_LINK = "linkHinh";
    private static final String HINH_PATH = "pathHinh";
    private Context mContext;

    public HinhDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_HINH = "CREATE TABLE " + TABLE_HINH + " ( "
                + HINH_ID_AUTO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HINH_ID + " TEXT, "
                + HINH_LINK + " TEXT, "
                + HINH_PATH + " TEXT );";
        db.execSQL(CREATE_TABLE_HINH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_HINH + " IF EXISTS");
        onCreate(db);
    }

    public boolean kiemTraHinhTonTai(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_HINH + " WHERE " + HINH_ID + " = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.getCount() != 0){
            return true;
        }
        return false;
    }

    public void themHinh(String id, String path, String link){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HINH_ID, id);
        values.put(HINH_PATH, path);
        values.put(HINH_LINK, link);
        db.insert(TABLE_HINH, null, values);
        db.close();
    }

    public String getPath(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_HINH + " WHERE " + HINH_ID + " = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        if(cursor.getCount() != 0){
            String path = cursor.getString(cursor.getColumnIndex(HINH_PATH));
            return path;
        }
        return "";
    }
}
