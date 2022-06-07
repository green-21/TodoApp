package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DataRepository {
    static DBHelper dbHelper;
    public static SQLiteDatabase DB;
    public static HashMap<Integer, TodoNode> todos;

    public static void initialize(Context context) {
        dbHelper = new DBHelper(context);
        DB = dbHelper.getWritableDatabase();
        todos = new HashMap<>();

//        rebuild();
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

    public static void updateTodoList(TodoList todoList) {
        DB.update("todo_list",
                todoList.getContentValues(),
                "uid="+todoList.uid, null);
    }

    public static void deleteTodoList(ArrayList<TodoList> lists, TodoList todoList) {
        DB.delete("todo_list", "uid=" + todoList.uid, null);
        DB.delete("todo_node", "belong=" + todoList.uid, null);
        ArrayList<TodoNode> temp = new ArrayList<>();

        for (TodoNode todo : todos.values()) {
            if (todo.belong == todoList.uid)
                temp.add(todo);
        }
        for (TodoNode todo : temp) {
            todos.remove(todo.uid);
        }
        lists.remove(todoList);
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

    public static void insertTodo(TodoList list, TodoNode todo) {
        int result = (int) DB.insert("todo_node",
                null, todo.getContentValues());
        todo.uid = result;
        list.insertTodo(todo);
        todos.put(todo.uid, todo);
    }

    public static void updateTodo(TodoNode todo) {
        DB.update("todo_node", todo.getContentValues(), "uid="+todo.uid, null);
    }

    public static void deleteTodo(TodoList list, TodoNode todo) {
        int result = DB.delete("todo_node","uid="+todo.uid, null);
            list.deleteTodo(todo);
            todos.remove(todo.uid);

    }
}
