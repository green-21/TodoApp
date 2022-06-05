package com.example.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;


public class TodoActivity extends Activity {
    EditText etTitle;
    EditText etMemo;
    Button btn;
    TodoNode todo;
    Switch today;
    Switch importance;
    LinearLayout dateLayout;
    TextView dateView;
    CalendarView calendar;
    Button dateBtn;
    String date;
    String dateTemp;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_todo);

        etTitle = (EditText) findViewById(R.id.etTodoTitle);
        etMemo = (EditText) findViewById(R.id.etTodoMemo);
        btn = (Button) findViewById(R.id.btnTodo);
        today = (Switch) findViewById(R.id.swToday);
        importance = (Switch) findViewById(R.id.swImportance);

        dateLayout = (LinearLayout) findViewById(R.id.dateLayout);
        dateView = (TextView) findViewById(R.id.tvDate);
        calendar = (CalendarView) findViewById(R.id.calendar);
        dateBtn = (Button) findViewById(R.id.btnDateSet);
        date = "";
        dateTemp ="";

        Intent intent = getIntent();
        int listID = intent.getIntExtra("list", -1);
        int todoID = intent.getIntExtra("todo", -1);

        if(listID == -1) {
            Toast.makeText(getApplicationContext(), "잘못된 목록을 접근했습니다.[5001]", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(todoID != -1) {
            todo = DataRepository.getTodoNode(listID, todoID);
            etTitle.setText(todo.name);
            etMemo.setText(todo.memo);
            today.setChecked(todo.today);
            importance.setChecked(todo.importance);
            date = todo.date;
            dateView.setText("시간 설정  "+ date);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    todo.name = etTitle.getText().toString();
                    todo.memo = etMemo.getText().toString();
                    todo.today = today.isChecked();
                    todo.importance = importance.isChecked();
                    DataRepository.updateTodoNode(listID, todo);
                    finish();
                }
            });
        } else {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    todo = new TodoNode(-1,
                            etTitle.getText().toString(),
                            listID,
                            false,
                            today.isChecked(),
                            importance.isChecked(),
                            date,
                            etMemo.getText().toString());
                    DataRepository.insertTodoNode(listID, todo);
                    finish();
                }
            });
        }

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "dd", Toast.LENGTH_SHORT).show();
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                dateTemp = year+ "-" + (month+1) + "-" + day;
            }
        });
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = dateTemp;
                dateView.setText("기간 설정   " + date);
                dateLayout.setVisibility(View.GONE);
            }
        });

    }
}
