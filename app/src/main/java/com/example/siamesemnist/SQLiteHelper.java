package com.example.siamesemnist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    static final String dbName = "HistoryDB.sqlite";
    private final String createDB = "CREATE TABLE IF NOT EXISTS HISTORY(Id INTEGER PRIMARY KEY AUTOINCREMENT, selectedNum VARCHAR, score VARCHAR, image BLOG)";
    private final String selectAll = "SELECT * FROM HISTORY";
    private final String insertData = "INSERT INTO HISTORY VALUES (NULL,?,?,?)";
    private final String deleteData = "DELETE FROM HISTORY WHERE id = ?";

    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String selectedNum, String score, byte[] image){
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteStatement statement = database.compileStatement(insertData);
        statement.clearBindings();
        statement.bindString(1, selectedNum);
        statement.bindString(2, score);
        statement.bindBlob(3, image);
        statement.executeInsert();
    }

    public Cursor getData(){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(selectAll, null);
    }

    public  void deleteData(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteStatement statement = database.compileStatement(deleteData);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);
        statement.execute();
        database.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.rawQuery(createDB, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
