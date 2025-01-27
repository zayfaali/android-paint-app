package com.example.paintapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PaintApp.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "Images";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE = "image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE + " BLOB)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Convert Bitmap to byte array

    public boolean saveImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, imageBytes);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1; // Returns true if the insert was successful
    }

//    public ArrayList<Bitmap> getAllImages() {
//        ArrayList<Bitmap> imageList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
//                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                imageList.add(bitmap);
//            }
//            cursor.close();
//        }
//        return imageList;
//    }

}
