package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class ListActivity extends AppCompatActivity {
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

        title = (TextView) findViewById(R.id.tvTodoListTitle);
        todos = (ListView) findViewById(R.id.todos);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        intent = getIntent();
        switch (intent.getIntExtra("mode", -1)) {
//            case 0:
//                todoList = todayMode();
//                break;
//            case 1:
//                todoList = importanceMode();
//                break;
//            case 2:
//                todoList = planMode();
//                break;
            default:
                todoList = todoMode();
                btnAdd.setVisibility(View.VISIBLE);
                break;
        }

        title.setText(todoList.name);
        adapter = new TodoListAdapter(this, todoList);
        todos.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        TodoActivity.class);
                intent.putExtra("list", todoList.uid);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        adapter.notifyDataSetChanged();
        super.onRestart();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

//    private TodoList todayMode() {
//        TodoList tl = new TodoList(-1, "오늘 할 일");
//        for (int key : DataRepository.todoLists.keySet()) {
//            for (TodoNode n : l.todos) {
//                if (n.today)
//                    tl.todos.add(n);
//            }
//        }
//        return tl;
//    }
//
//    private TodoList importanceMode() {
//        TodoList tl = new TodoList(-1, "중요한 일");
//        for (TodoList l : DataRepository.todoLists) {
//            for (TodoNode n : l.todos) {
//                if (n.importance)
//                    tl.todos.add(n);
//            }
//        }
//        return tl;
//    }
//
//    private TodoList planMode() {
//        TodoList tl = new TodoList(-1, "계획된 일정");
//        for (TodoList l : DataRepository.todoLists) {
//            for (TodoNode n : l.todos) {
//                if (!n.date.equals(""))
//                    tl.todos.add(n);
//            }
//        }
//        return tl;
//    }

    private TodoList todoMode() {
        TodoList tl = DataRepository.getTodoList(intent.getIntExtra("uid", -1));
        if (tl == null) {
            Toast.makeText(getApplicationContext(), "잘못된 요청입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        return tl;
    }
}
