package com.example.todo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class MainTodoListAdapter extends BaseAdapter {
    ArrayList<TodoList> lists;
    Context context;

    public MainTodoListAdapter(Context context, ArrayList<TodoList> list) {
        this.context = context;
        this.lists = list;
    }

    @Override
    public int getCount() {
        return lists.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return lists.indexOf(i);
    }

    @Override
    public long getItemId(int i) {
        return lists.get(i).uid;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i == getCount() - 1) {
            return new TodoListButton(context, this, lists, null);
        }
        return new TodoListButton(context, this, lists, lists.get(i));
    }
}