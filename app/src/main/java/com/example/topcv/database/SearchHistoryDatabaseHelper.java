package com.example.topcv.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "search_history.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "search_history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_KEYWORD = "keyword";
    private static final String COLUMN_TIMESTAMP = "timestamp";  // Để lưu thời gian tìm kiếm

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

        // Kiểm tra nếu từ khóa đã tồn tại
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_KEYWORD + " = ?", new String[]{keyword}, null, null, null);
        if (cursor.moveToFirst()) {
            // Nếu tồn tại, cập nhật lại thời gian
            ContentValues values = new ContentValues();
            values.put(COLUMN_TIMESTAMP, System.currentTimeMillis());
            db.update(TABLE_NAME, values, COLUMN_KEYWORD + " = ?", new String[]{keyword});
        } else {
            // Nếu chưa tồn tại, thêm mới
            ContentValues values = new ContentValues();
            values.put(COLUMN_KEYWORD, keyword);
            db.insert(TABLE_NAME, null, values);
        }

        // Xóa các từ khóa cũ nếu vượt quá 10 từ khóa
        String deleteOldKeywords = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID +
                " NOT IN (SELECT " + COLUMN_ID + " FROM " + TABLE_NAME +
                " ORDER BY " + COLUMN_TIMESTAMP + " DESC LIMIT 10)";
        db.execSQL(deleteOldKeywords);

        cursor.close();
        db.close();
    }


    // Trong SearchHistoryDatabaseHelper
    public List<String> getRecentKeywords() {
        List<String> keywords = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn lấy ra 10 từ khóa gần đây nhất
        String selectQuery = "SELECT keyword FROM search_history ORDER BY id DESC LIMIT 10";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int columnIndex = cursor.getColumnIndex("keyword");
                if (columnIndex != -1) {
                    String keyword = cursor.getString(columnIndex);
                    keywords.add(keyword);  // Lưu từng từ khóa vào danh sách
                }
            } while (cursor.moveToNext());
        }

        cursor.close();  // Đảm bảo đóng cursor sau khi truy vấn
        db.close();      // Đóng database

        return keywords;  // Trả về danh sách từ khóa
    }
}
