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
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        CheckBox cb = view.findViewById(R.id.itemCheck);
        TextView itemName = view.findViewById(R.id.itemName);
        TextView itemToday = view.findViewById(R.id.itemToday);
        TextView itemImpotance = view.findViewById(R.id.itemImportance);
        TextView itemDate = view.findViewById(R.id.itemDate);
        GridLayout item = (GridLayout) view.findViewById(R.id.itemLayout);
        LinearLayout itemDetail = (LinearLayout) view.findViewById(R.id.itemDetail);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b != todo.checked) {
                    todo.checked = b;
                    DataRepository.updateTodo(todo);
                }
                setThroughLine(itemName, b);
                setThroughLine(itemToday, b);
                setThroughLine(itemImpotance,b);
                setThroughLine(itemDate,b);
            }
        });
        item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogManager menu = new DialogManager(context);
                menu.showMenuDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent intent = new Intent(context, TodoNodeActivity.class);
                            intent.putExtra("uid", todo.uid);
                            ((AppCompatActivity) context).startActivityForResult(intent, 0);
                        } else {
                            DialogManager deleteDialog = new DialogManager(context);
                            deleteDialog.showDeleteCheckDialog(
                                    context.getResources().getString(R.string.todo_delete_msg),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            DataRepository.deleteTodo(todoList, todo);
                                            notifyDataSetChanged();
                                        }
                                    });

                        }
                    }
                });
                return false;
            }
        });
        itemName.setText(todo.name);
        cb.setChecked(todo.checked);
        boolean isDetail = false;

        isDetail |= visibleText(itemToday, todo.today);
        isDetail |= visibleText(itemImpotance, todo.importance);

        if(!todo.date.equals("")) {
            itemDate.setText("\uD83D\uDDD3" + todo.date);
            isDetail = true;
        }
        if(isDetail)
            itemDetail.setVisibility(View.VISIBLE);
        else
            itemDetail.setVisibility(View.GONE);

        return view;
    }

    private void setThroughLine(TextView tv, boolean b) {
        if (b) tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
    }
    private boolean visibleText(TextView tv, boolean b) {
        if (b)
            tv.setVisibility(View.VISIBLE);
        else
            tv.setVisibility(View.GONE);
        return b;
    }
}

