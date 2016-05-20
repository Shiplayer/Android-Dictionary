package com.advanced.java.ship.dictionary.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Anton on 18.05.2016.
 */
public class SQLWordsHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "TrWords";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_TYPE_ID = " integer";
    public static final String COLUMN_NAME_WORD = "Word";
    public static final String COLUMN_TYPE_WORD = " varchar(256)";
    public static final String COLUMN_NAME_TRWORD = "TrWord";
    public static final String COLUMN_TYPE_TRWORD = " varchar(2048)";
    public static final String COLUMN_NAME_TSWORD = "TsWord";
    public static final String COLUMN_TYPE_TSWORD = " varchar(256)";

    public SQLWordsHelper(Context context, int version) {
        super(context, TABLE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("SQLite", "Create table");
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_NAME_ID + COLUMN_TYPE_ID + "," +
                    COLUMN_NAME_WORD + COLUMN_TYPE_WORD + "," +
                    COLUMN_NAME_TRWORD + COLUMN_TYPE_TRWORD + "," +
                    COLUMN_NAME_TSWORD + COLUMN_TYPE_TSWORD +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("SQLite", "Upgrade table");
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_TSWORD + COLUMN_TYPE_TSWORD + " DEFAULT 'none'");
        }

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
