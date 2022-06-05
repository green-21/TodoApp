package com.example.todo;

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
}
