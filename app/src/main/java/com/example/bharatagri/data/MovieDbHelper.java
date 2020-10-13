package com.example.bharatagri.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "document.db";
    private static final int DATABASE_VERSION = 1;


    public MovieDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_MOVIE_TABLE =  "CREATE TABLE " + MovieContract.TableName + " ("
                + MovieContract._ID + " INTEGER PRIMARY KEY, "
                + MovieContract.Description + " varchar(10), "
                + MovieContract.Rating + " INTEGER, "
                + MovieContract.ImageUrl + " VARCHAR(200), "
                + MovieContract.ImageByte + " BLOB, "
                + MovieContract.Title + " VARCHAR(30));";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
