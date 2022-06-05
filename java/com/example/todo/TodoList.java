package com.example.todo;

import java.util.ArrayList;
import java.util.HashMap;

public class TodoList {
    public int uid;
    public String name;
    public HashMap<Integer, TodoNode> todos;

    public TodoList(int uid, String name) {
        this.uid = uid;
        this.name = name;
        todos = new HashMap<>();

    }
    public TodoList(int uid, String name, HashMap<Integer,TodoNode> todos) {
        this.uid = uid;
        this.name = name;
        this.todos = todos;
    }
}
