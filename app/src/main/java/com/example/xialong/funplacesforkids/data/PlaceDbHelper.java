package com.example.xialong.funplacesforkids.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlaceDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "place.db";

    public PlaceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE =
                "CREATE TABLE " + PlaceContract.PlaceEntry.TABLE_NAME + " (" +
                        PlaceContract.PlaceEntry.COLUMN_PLACE_ID + " TEXT PRIMARY KEY, " +
                        PlaceContract.PlaceEntry.COLUMN_PLACE_NAME + " TEXT NOT NULL, " +
                        PlaceContract.PlaceEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                        PlaceContract.PlaceEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                        PlaceContract.PlaceEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
                        PlaceContract.PlaceEntry.COLUMN_LATITUDE + " REAL NOT NULL, " +
                        PlaceContract.PlaceEntry.COLUMN_LONGITUDE + " REAL NOT NULL, " +
                        PlaceContract.PlaceEntry.COLUMN_ISFAV + " INTEGER NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PlaceContract.PlaceEntry.TABLE_NAME);
    }
}
