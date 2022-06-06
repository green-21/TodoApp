package com.example.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class DialogManager {
    Context context;
    AlertDialog.Builder dialog;
    String result;

    public DialogManager(Context context) {
        dialog = new AlertDialog.Builder(context);
        this.context = context;
    }
    // 제목 입력 및 수정 다이얼로그

    public void showTodoListTitleEditorDialog(ArrayList<TodoList> lists, TodoList tl) {
        View dView = (View)View.inflate(context, R.layout.dialog_todolist, null);
        String name = "";

        EditText eText = (EditText) dView.findViewById(R.id.edtListName);

        dialog.setTitle("생성");
        if(tl != null) {
            name = tl.name;
            dialog.setTitle("수정");
        }
        eText.setText(name);
        dialog.setView(dView);

        dialog.setPositiveButton("완료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String text = eText.getText().toString();
                if (text.equals(""))
                    text = " ";

                if (tl == null) {
                    TodoList newTl = new TodoList(text);
                    DataRepository.insertTodoList(lists, newTl);
                } else {
                    tl.name = text;
                    DataRepository.updateTodoList(tl);
                }
            }
        });
        dialog.setNegativeButton("취소", null);
        Object t = dialog.show();
    }

    public void showMenuDialog(DialogInterface.OnClickListener listener) {
        dialog.setTitle(R.string.menu_title);
        dialog.setItems(new String[] {"수정", "삭제"}, listener);
        dialog.show();
    }

    public void showDeleteCheckDialog(String msg, DialogInterface.OnClickListener posListener) {
        dialog.setTitle("삭제");
        dialog.setMessage(msg);

        dialog.setPositiveButton("완료", posListener);
        dialog.setNegativeButton("취소", null);
        dialog.show();
    }
}
