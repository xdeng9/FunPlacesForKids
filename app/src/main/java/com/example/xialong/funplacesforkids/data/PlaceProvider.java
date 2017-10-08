package com.example.xialong.funplacesforkids.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
        mDbHelper = new PlaceDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case PLACE: {
                cursor = mDbHelper.getReadableDatabase().query(
                        PlaceContract.PlaceEntry.TABLE_NAME,
                        strings,
                        s,
                        strings1,
                        null,
                        null,
                        s1
                );
                break;
            }
            case PLACE_WITH_ID: {
                cursor = mDbHelper.getReadableDatabase().query(
                        PlaceContract.PlaceEntry.TABLE_NAME,
                        null,
                        sSelectionWithId,
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        s1
                );
                break;
            }
            default: throw new UnsupportedOperationException("Unkown uri: "+ uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values){
        int rowsInserted = 0;
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){
            case PLACE:
                db.beginTransaction();
                try{
                    for(ContentValues contentValues : values){
                        long id = db.insertWithOnConflict(PlaceContract.PlaceEntry.TABLE_NAME, null,
                                contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                        if(id != -1){
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally{
                    db.endTransaction();
                }

                if(rowsInserted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
        }

        return rowsInserted;
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
        int numOfRowsDeleted;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        if(sUriMatcher.match(uri) == PLACE_WITH_ID){
            String id = uri.getPathSegments().get(1);
            numOfRowsDeleted = db.delete(PlaceContract.PlaceEntry.TABLE_NAME, sSelectionWithId, new String[]{id});
        }else{
            numOfRowsDeleted = db.delete(PlaceContract.PlaceEntry.TABLE_NAME, sFavSelection, new String[]{"0"});
        }
        if(numOfRowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numOfRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int rowsUpdated;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        if(sUriMatcher.match(uri) == PLACE_WITH_ID){
            String id = uri.getPathSegments().get(1);
            rowsUpdated = db.update(PlaceContract.PlaceEntry.TABLE_NAME, contentValues, sSelectionWithId,
                    new String[]{id});
        } else {
            rowsUpdated = db.update(PlaceContract.PlaceEntry.TABLE_NAME, contentValues, s, strings);
        }
        return rowsUpdated;
    }
}
