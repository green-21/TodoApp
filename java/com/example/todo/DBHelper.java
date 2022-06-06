package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "todoData", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL("CREATE TABLE todo_list (uid INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, special BOOLEAN);");
        db.execSQL("CREATE TABLE todo_node (" +
                "uid INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name TEXT," +
                " belong INTEGER not null," +
                " checked BOOLEAN," +
                " today BOOLEAN," +
                " importance BOOLEAN," +
                " date TEXT," +
                " memo TEXT," +
                " FOREIGN KEY(belong) REFERENCES todo_list(uid) on delete cascade);");

        String[] str = {"오늘 할 일", "중요", "일정"};
        for (String s : str) {
            ContentValues cv = new ContentValues();
            cv.put("name", s);
            cv.put("special", true);
            db.insert("todo_list", null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS todo_list");
        db.execSQL("DROP TABLE IF EXISTS todo_node");
        onCreate(db);
    }
}
