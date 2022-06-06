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
    public static HashMap<Integer, TodoNode> todos;

    public static void initialize(Context context) {
        dbHelper = new DBHelper(context);
        DB = dbHelper.getWritableDatabase();
        todos = new HashMap<>();

        //rebuild();
        // todos 데이터 입력

        Cursor cursor = DB.rawQuery("SELECT * FROM todo_node;", null);
        while(cursor.moveToNext()) {
            TodoNode todo = new TodoNode(cursor);
            todos.put(todo.uid, todo);
        }
    }
    public static void rebuild() {
        dbHelper.onUpgrade(DB, 1,1);
    }

    public static ArrayList<TodoList> getTodoLists() {
        ArrayList<TodoList> todoLists = new ArrayList<>();
        Cursor cursor = DB.rawQuery("SELECT * FROM todo_list", null);
        while (cursor.moveToNext()) {
            TodoList tl = new TodoList(cursor);
            todoLists.add(tl);
        }
        return todoLists;
    }

    public static boolean insertTodoList(ArrayList<TodoList> lists, TodoList todoList) {
        int result = (int) DB.insert("todo_list", null, todoList.getContentValues());
        if (result != -1) {
            todoList.uid = result;
            lists.add(todoList);
            return true;
        }
        return false;
    }

    public static boolean updateTodoList(TodoList todoList) {
        return DB.update("todo_list",
                todoList.getContentValues(),
                "uid="+todoList.uid, null) ==1;
    }

    public static boolean deleteTodoList(ArrayList<TodoList> lists, TodoList todoList) {
        int i = DB.delete("todo_list","uid="+todoList.uid, null);
        boolean result = i==1;
        if(result) {
            lists.remove(todoList);
        }
        return result;
    }

    public static TodoList getTodoList(int uid) {
        String where;
        TodoList todoList = null;

        Cursor listCursor = DB.rawQuery("SELECT * FROM todo_list WHERE uid="+uid, null);
        while (listCursor.moveToNext()) {
            todoList = new TodoList(listCursor);
        }

        for(TodoNode todo : todos.values()) {
            switch(uid) {
                case 1:
                    if(todo.today)
                        todoList.todos.add(todo);
                    break;
                case 2:
                    if(todo.importance)
                        todoList.todos.add(todo);
                    break;
                case 3:
                    if(!todo.date.equals(""))
                        todoList.todos.add(todo);
                    break;
                default:
                    if(uid == todo.belong)
                        todoList.todos.add(todo);
                    break;
            }
        }
        return todoList;
    }

    public static TodoNode getTodo(int uid) {
        return todos.get(uid);
    }

    public static boolean insertTodo(TodoList list, TodoNode todo) {
        int result = (int) DB.insert("todo_node",null, todo.getContentValues());
        if (result != -1) {
            todo.uid = result;
            list.insertTodo(todo);
            todos.put(todo.uid, todo);
            return true;
        }
        return false;
    }

    public static boolean updateTodo(TodoNode todo) {

        return DB.update("todo_node", todo.getContentValues(), "uid="+todo.uid, null) == 1;
    }

    public static boolean deleteTodo(TodoList list, TodoNode todo) {
        int result = DB.delete("todo_node","uid="+todo.uid, null);
        if (result == 1) {
            list.deleteTodo(todo);
            todos.remove(todo.uid);
            return true;
        }
        return false;
    }
}
