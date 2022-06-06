package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {
    MainTodoListAdapter gAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("TODO");

        DataRepository.initialize(this);

        GridView gv = (GridView) findViewById(R.id.gView1);

        gAdapter = new MainTodoListAdapter(this, DataRepository.getTodoLists());
        gv.setAdapter(gAdapter);
    }
}