package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class TodoListActivity extends AppCompatActivity {
    TodoList todoList;
    TodoListAdapter adapter;
    Intent intent;
    TextView title;
    ListView todos;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle("목록");
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setElevation(0);

        Intent intent = getIntent();
        int listID = intent.getIntExtra("uid", -1);
        if (listID == -1)
            finish();

        todoList = DataRepository.getTodoList(listID);
        title = (TextView) findViewById(R.id.tvTodoListTitle);
        todos = (ListView) findViewById(R.id.todos);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        title.setText(todoList.name);
        adapter = new TodoListAdapter(this, todoList);
        todos.setAdapter(adapter);

        if (!todoList.special)
            btnAdd.setVisibility(View.VISIBLE);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        TodoNodeActivity.class);
                intent.putExtra("list", todoList.uid);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onStart() {
        adapter.notifyDataSetChanged();
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        if(result != RESULT_OK) return;

        int uid = data.getIntExtra("uid", -1);
        TodoNode t = new TodoNode(uid,
                data.getStringExtra("name"),
                todoList.uid,
                false,
                data.getBooleanExtra("today", false),
                data.getBooleanExtra("importance", false),
                data.getStringExtra("data"),
                data.getStringExtra("memo"));
        if (uid == -1) {
            DataRepository.insertTodo(todoList, t);
        } else {
            TodoNode todo = DataRepository.getTodo(uid);
            todo.name = t.name;
            todo.today = t.today;
            todo.importance = t.importance;
            todo.date = t.date;
            todo.memo = t.memo;
            DataRepository.updateTodo(todo);
            if (todoList.special) {
                todoList.todos = DataRepository.getTodoList(todoList.uid).todos;
            }
        }
        adapter.notifyDataSetChanged();
    }
}
