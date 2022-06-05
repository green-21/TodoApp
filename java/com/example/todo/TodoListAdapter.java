package com.example.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class TodoListAdapter extends BaseAdapter {
    TodoList todoList;
    Context context;
    public TodoListAdapter(Context c, TodoList tl) {
        context = c;
        todoList = tl;
    }

    @Override
    public int getCount() {
        return todoList.todos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TodoNode todo = todoList.todos.get(i);

        if (view == null) {
            Context context = viewGroup.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_todos, viewGroup, false);
        }

        CheckBox cb = view.findViewById(android.R.id.text1);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b != todo.checked) {
                    todo.checked = b;
                    if (DataRepository.updateTodoNode(todo.belong, todo) == -1)
                        Toast.makeText(context, "업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
                if (b) cb.setPaintFlags(cb.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                else cb.setPaintFlags(cb.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });
        cb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showMenuDialog(todo);
                return false;
            }
        });
        cb.setText(todo.name);
        cb.setChecked(todo.checked);
        return view;
    }

    private void showMenuDialog(TodoNode todo) {
        String[] actionItems = new String[]{"수정", "삭제"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("기능 선택");

        dialog.setItems(actionItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) showUpdateIntent(todo.belong, todo);
                else {
                    showDeleteDialog(todo.belong, todo.uid);
                }

            }
        });
        dialog.show();
    }

    private void showUpdateIntent(int listID, TodoNode todo) {
        Intent intent = new Intent(context,
                TodoActivity.class);
        intent.putExtra("list", todo.belong);
        intent.putExtra("todo", todo.uid);
        context.startActivity(intent);
    }
    private void showDeleteDialog(int listID, int todoID) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("삭제하시겠습니까?");
        dialog.setMessage("작업이 영원히 삭제됩니다. 계속하시겠습니까?");
        dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int result = DataRepository.deleteTodoNode(listID, todoID);
                if(result == -1) {
                    Toast.makeText(context,
                            "목록 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                listUpdate(listID, todoID);
                // 독립 리스트 처리
                notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("취소", null);
        dialog.show();
    }

    private void listUpdate(int listID, int todoID) {
        if (todoList.uid == -1) {
            todoList.todos.remove(todoID);
        }
    }
}
