package com.example.todo;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

public class TodoList {
    public int uid;
    public String name;
    public boolean special;
    public ArrayList<TodoNode> todos;

    public TodoList(String name) {
        this.name = name;
        this.special = false;
        todos = new ArrayList<>();
    }

    public TodoList(int uid, String name, boolean special) {
        this.uid = uid;
        this.name = name;
        this.special = special;
        todos = new ArrayList<>();
    }

    public TodoList(Cursor cursor) {
        this.uid = cursor.getInt(0);
        this.name = cursor.getString(1);
        this.special = cursor.getInt(2) != 0;
        todos = new ArrayList<>();
    }

    public void insertTodo(TodoNode todo) {
        todos.add(todo);
    }

    public void deleteTodo(TodoNode todo) {
        todos.remove(todo);
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("special", special);
        return cv;
    }
}
