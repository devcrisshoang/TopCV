package com.example.topcv.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class SearchHistoryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "search_history.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "search_history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_KEYWORD = "keyword";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    public SearchHistoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_KEYWORD + " TEXT, "
                + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addKeyword(String keyword) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_KEYWORD + " = ?", new String[]{keyword}, null, null, null);
        if (cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TIMESTAMP, System.currentTimeMillis());
            db.update(TABLE_NAME, values, COLUMN_KEYWORD + " = ?", new String[]{keyword});
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_KEYWORD, keyword);
            db.insert(TABLE_NAME, null, values);
        }

        String deleteOldKeywords = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID +
                " NOT IN (SELECT " + COLUMN_ID + " FROM " + TABLE_NAME +
                " ORDER BY " + COLUMN_TIMESTAMP + " DESC LIMIT 10)";
        db.execSQL(deleteOldKeywords);

        cursor.close();
        db.close();
    }

    public List<String> getRecentKeywords() {
        List<String> keywords = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT keyword FROM search_history ORDER BY id DESC LIMIT 10";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int columnIndex = cursor.getColumnIndex("keyword");
                if (columnIndex != -1) {
                    String keyword = cursor.getString(columnIndex);
                    keywords.add(keyword);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return keywords;
    }
}
