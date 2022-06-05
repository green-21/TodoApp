package com.example.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;

public class ListGridAdapter extends BaseAdapter {
    Context context;

    public ListGridAdapter(Context c) {
        context = c;
    }
    @Override
    public int getCount() {
        return DataRepository.getCountTodoList() + 4;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Button btn = new Button(context);
        switch(i) {
            case 0:
            case 1:
            case 2:
                setDefaultButton(i, btn);
                break;
            default:
                if(i == getCount()-1)
                    setAddButton(btn);
                else setGridButton(i, btn);

        }
        // 마지막 버튼일 경우는 새로 만들기 버튼
//        if(i == DataRepository.getCountTodoList())
//            setAddButton(i, btn);
//        else setGridButton(i,btn);

        btn.setLayoutParams(new ViewGroup.LayoutParams(500,400));
        btn.setTextSize(20);
        btn.setPadding(10,10,10,10);

        return btn;
    }

    // 버튼 생성 함수
    private void setDefaultButton(int i, Button btn) {
        String[] text = {"오늘", "중요", "일정"};
        btn.setText(text[i]);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListActivity.class);
                intent.putExtra("mode", i);
                context.startActivity(intent);
            }
        });
    }


    private void setGridButton(int i, Button btn) {
        Object[] keys = DataRepository.todoLists.keySet().toArray();
        Arrays.sort(keys);
        TodoList tl = DataRepository.getTodoList((int)keys[i-3]);


        // 목록 상세로 넘어감
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,
                        ListActivity.class);
                intent.putExtra("uid", tl.uid);
                context.startActivity(intent);
            }
        });

        // 롤클릭 - 수정 & 삭제
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showMenuDialog(tl);
                return false;
            }
        });
    }

    private void setAddButton(Button btn) {
        btn.setText("+");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListDialog(-1, "새 목록 생성", "");
            }
        });
    }

    private void showMenuDialog(TodoList tl) {
        String[] actionItems = new String[] {"수정", "삭제"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("기능 선택");

        dialog.setItems(actionItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i ==0) showListDialog(tl.uid, "목록 이름 수정", tl.name);
                else showDeleteDialog(tl);
            }
        });
        dialog.show();

    }

    private void showDeleteDialog(TodoList tl) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("삭제하시겠습니까?");
        dialog.setMessage("목록에 포함된 모든 할 일이 모두 영구 삭제됩니다. 그래도 삭제하시겠습니까?");
        dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int result = DataRepository.deleteTodoList(tl);
                if(result == -1) {
                    Toast.makeText(context,
                            "목록 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                notifyDataSetChanged();

            }
        });
        dialog.setNegativeButton("취소", null);
        dialog.show();
    }

    public void showListDialog(int listID, String title, String name) {
        View dView = (View) View.inflate(context, R.layout.dialog_todolist, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        EditText eText = (EditText) dView.findViewById(R.id.edtListName);
        eText.setText(name);
        dialog.setTitle(title);
        dialog.setView(dView);

        String[] okText = new String[] {"생성", "수정"};
        DialogInterface.OnClickListener[] listeners = new DialogInterface.OnClickListener[] {
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = eText.getText().toString();
                        long result = DataRepository.insertTodoList(name);
                        notifyDataSetChanged();
                        if (result == -1)
                            Toast.makeText(context,
                                    "목록 생성에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = eText.getText().toString();
                        int result = DataRepository.updateTodoList(listID, name);
                        notifyDataSetChanged();
                        if (result == -1)
                            Toast.makeText(context,
                                    "목록 수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        };
        int mode = listID == -1 ? 0 : 1;
        dialog.setPositiveButton(okText[mode], listeners[mode]);
        dialog.setNegativeButton("취소", null);
        dialog.show();
    }
}