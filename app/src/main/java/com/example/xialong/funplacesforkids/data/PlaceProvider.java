package com.example.xialong.funplacesforkids.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PlaceProvider extends ContentProvider{

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int PLACE = 1;
    private static final int PLACE_WITH_ID = 2;
    private PlaceDbHelper mDbHelper;
    private static final String sSelectionWithId = PlaceContract.PlaceEntry.TABLE_NAME+"."+
            PlaceContract.PlaceEntry.COLUMN_PLACE_ID + " = ?";
    private static final String sFavSelection = PlaceContract.PlaceEntry.TABLE_NAME+"."+
            PlaceContract.PlaceEntry.COLUMN_ISFAV + "= ?";

    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = PlaceContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, PlaceContract.PATH, PLACE);
        uriMatcher.addURI(authority, PlaceContract.PATH + "/*", PLACE_WITH_ID);

        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implementing getType.");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        throw new RuntimeException("Not implementing insert.");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
