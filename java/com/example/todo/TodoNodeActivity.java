package com.example.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;


public class TodoNodeActivity extends Activity {
    EditText etTitle, etMemo;
    Button btn;
    TodoNode todo;
    Switch today, importance;
    LinearLayout dateLayout;
    TextView dateView;
    CalendarView calendar;
    Button dateBtn, dateReBtn;
    String date, dateTemp;
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
        dateReBtn = (Button) findViewById(R.id.btnDateReset);
        date = "";
        dateTemp = "";

        Intent intent = getIntent();
        int uid = intent.getIntExtra("uid", -1);

        // 생성
        if (uid != -1) {
            TodoNode todo = DataRepository.getTodo(uid);
            etTitle.setText(todo.name);
            etMemo.setText(todo.memo);
            today.setChecked(todo.today);
            importance.setChecked(todo.importance);
            dateView.setText("기간 설정  " + todo.date);
            date = todo.date;
        }

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateLayout.setVisibility(View.VISIBLE);
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                dateTemp = year + "-" + (month + 1) + "-" + day;
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

        dateReBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date="";
                dateTemp="";
                dateView.setText("기간 설정");
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent out = new Intent(getApplicationContext(), TodoListActivity.class);
                out.putExtra("uid", uid);
                out.putExtra("name", etTitle.getText().toString());
                out.putExtra("memo", etMemo.getText().toString());
                out.putExtra("today", today.isChecked());
                out.putExtra("importance", importance.isChecked());
                out.putExtra("data", date);
                setResult(RESULT_OK, out);
                finish();
            }
        });

    }
}
