package com.example.xialong.funplacesforkids;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.xialong.funplacesforkids.data.PlaceContract;
import com.example.xialong.funplacesforkids.data.PlaceDbHelper;

import java.util.HashSet;

public class TestDb extends AndroidTestCase{

    public static final String TAG = TestDb.class.getSimpleName();

    void deleteDatabase(){
        mContext.deleteDatabase(PlaceDbHelper.DATABASE_NAME);
    }

    public void setUp(){
        deleteDatabase();
    }

    public void testCreateDb() throws Throwable{
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(PlaceContract.PlaceEntry.TABLE_NAME);

        mContext.deleteDatabase(PlaceDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new PlaceDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());
    }
}
