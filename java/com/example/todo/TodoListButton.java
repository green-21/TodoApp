package com.example.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class TodoListButton extends androidx.appcompat.widget.AppCompatButton {
    BaseAdapter parent;
    public TodoListButton(Context context, BaseAdapter parent, ArrayList<TodoList> lists, TodoList todoList) {
        super(context);
        this.parent = parent;
        setLayoutParams(new ViewGroup.LayoutParams(500, 400));
        setTextSize(20);
        setPadding(10, 10, 10, 10);

        if (todoList == null) {
            setText("+");
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogManager dialog = new DialogManager(context);
                    dialog.showTodoListTitleEditorDialog(lists, null);
                }
            });
            return;
        }

        setText(todoList.name);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TodoListActivity.class);
                intent.putExtra("uid", todoList.uid);
                context.startActivity(intent);
            }
        });
        if (todoList.special)
            return;

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogManager menu = new DialogManager(context);
                menu.showMenuDialog(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        DialogManager editDialog = new DialogManager(context);
                                        editDialog.showTodoListTitleEditorDialog(lists, todoList);
                                        break;
                                    case 1:
                                        DialogManager deleteDialog = new DialogManager(context);
                                        deleteDialog.showDeleteCheckDialog(
                                                context.getResources().getString(R.string.list_delete_msg),
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        DataRepository.deleteTodoList(lists, todoList);
                                                        parent.notifyDataSetChanged();
                                                    }
                                                });
                                        break;
                                }
                            }
                        });
                return false;
            }
        });

    }
}
