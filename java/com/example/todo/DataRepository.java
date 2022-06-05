package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class DataRepository {
    static DBHelper dbHelper;
    public static SQLiteDatabase DB;
    public static HashMap<Integer, TodoList> todoLists;

    public static void initialize(Context context) {
        dbHelper = new DBHelper(context);
        DB = dbHelper.getWritableDatabase();
        todoLists = new HashMap<>();

        // table rebuild
//        dbHelper.onUpgrade(DB,1,1);

        // db 데이터를 받아와서 todoList에 대입
        Cursor listCursor = DB.rawQuery("SELECT * FROM todo_list ORDER BY uid ASC", null);
        while(listCursor.moveToNext()) {
            TodoList tl = new TodoList(
                    listCursor.getInt(0),
                    listCursor.getString(1));
            todoLists.put(tl.uid, tl);
        }
        listCursor.close();

        // todoNode 대입
        Cursor nodeCursor = DB.rawQuery("SELECT * FROM todo_node ORDER BY belong ASC", null);
        while(nodeCursor.moveToNext()) {
            //int uid, String name, boolean checked, boolean today, boolean importance, String date, String memo

            TodoNode tn = new TodoNode(
                    nodeCursor.getInt(0),
                    nodeCursor.getString(1),
                    nodeCursor.getInt(2),
                    nodeCursor.getInt(3) != 0,
                    nodeCursor.getInt(4) != 0,
                    nodeCursor.getInt(5) != 0,
                    nodeCursor.getString(6),
                    nodeCursor.getString(7)
            );
            todoLists.get(tn.belong).todos.put(tn.uid,tn);
        }
    }

    public static void close() {
        DB.close();
    }

    public static long insertTodoList(String name) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        long result = DB.insert("todo_list", null, cv);
        if (result != -1) {
            TodoList tl = new TodoList((int)result, name);
            todoLists.put(tl.uid, tl);
        }
        return result;
    }

    public static int updateTodoList(int uid, String name) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        int result = DB.update("todo_list", cv, "uid=" + uid, null);
        if (result > 0) {
            getTodoList(uid).name = name;
            return 0;
        }
        return -1;
    }

    public static int deleteTodoList(TodoList tl) {
        for (int key : tl.todos.keySet()) {
            tl.todos.remove(key);
        }

        int result = DB.delete("todo_list", "uid=" + tl.uid, null);
        if (result > 0) {
            todoLists.remove(tl);
            return result;
        }
        return -1;
    }

    public static int getCountTodoList() {
        return todoLists.size();
    }

    public static TodoList getTodoList(int uid) {
        return todoLists.get(uid);
    }

    public static long insertTodoNode(int parent, TodoNode tn) {
        ContentValues cv = nodeToContentValues(tn);

        long result = DB.insert("todo_node", null, cv);
        if(result != -1)
            tn.uid = (int)result;
            getTodoList(parent).todos.put(tn.uid, tn);

        return result;
    }

    public static int updateTodoNode(int parent, TodoNode tn) {
        ContentValues cv = nodeToContentValues(tn);

        int result = DB.update("todo_node", cv, "uid="+tn.uid, null);
        if (result > 0) {
            return 0;
        }
        return -1;
    }

    public static int deleteTodoNode(int listID, int todoID) {
        int result = DB.delete("todo_node", "uid="+todoID, null);
        if (result > 0) {
            TodoList tl = getTodoList(listID);
            TodoNode node = getTodoNode(tl, todoID);
            tl.todos.remove(node);
            return 0;
        }
        return -1;
    }

    public static TodoNode getTodoNode(int listId, int todoID) {
        TodoList tl = getTodoList(listId);
        return getTodoNode(tl, todoID);
    }
    public static TodoNode getTodoNode(TodoList todoList, int todoID) {
        return todoList.todos.get(todoID);
    }

    static ContentValues nodeToContentValues(TodoNode tn) {
        ContentValues cv = new ContentValues();
        cv.put("name", tn.name);
        cv.put("belong", tn.belong);
        cv.put("checked", tn.checked);
        cv.put("today", tn.today);
        cv.put("importance", tn.importance);
        cv.put("date", tn.date);
        cv.put("memo", tn.memo);
        return cv;
    }

}
