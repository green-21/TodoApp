package com.example.todo;

import android.content.ContentValues;
import android.database.Cursor;

public class TodoNode {
    public int uid;
    public String name;
    public int belong;
    public boolean checked;
    public boolean today;
    public boolean importance;
    public String date;
    public String memo;

    public TodoNode(int uid, int belong, String name, String date) {
        this(uid, name, belong, false, false, false, date, "");
    }

    public TodoNode(int uid, String name, int belong, boolean checked, boolean today, boolean importance, String date, String memo) {
        this.uid = uid;
        this.name = name;
        this.belong = belong;
        this.checked = checked;
        this.today = today;
        this.importance = importance;
        this.date = date;
        this.memo = memo;
    }

    public TodoNode(Cursor c) {
        this.uid = c.getInt(0);
        this.name = c.getString(1);
        this.belong = c.getInt(2);
        this.checked = c.getInt(3) != 0;
        this.today = c.getInt(4) != 0;
        this.importance = c.getInt(5) != 0;
        this.date = c.getString(6);
        this.memo = c.getString(7);
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("belong", belong);
        cv.put("checked", checked);
        cv.put("today", today);
        cv.put("importance", importance);
        cv.put("date", date);
        cv.put("memo", memo);
        return cv;
    }
}
