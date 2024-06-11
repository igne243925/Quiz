package com.jtdev.knowsalot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jtdev.knowsalot.Adapters.QuizzesAdapter;
import com.jtdev.knowsalot.Adapters.StudentQuizzesAdapter;

import java.util.ArrayList;
import java.util.List;

public class StudentQuizzesScreen extends AppCompatActivity {

    RecyclerView recyclerView;
    StudentQuizzesAdapter adapter;
    int spanCount = 2;
    Button backbutton;

    private static final String PREF_NAME = "QuizListPrefs";
    private static final String KEY_QUIZ_LIST = "quizList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quizzes_screen);

        backbutton = findViewById(R.id.returnBtn);
//back btn
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentQuizzesScreen.this,PlayerScreen.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.quizRecycler);
        adapter = new StudentQuizzesAdapter(getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));

    }

  // dito yung mga nacreaate sa custom quiz
    private List<String> getData() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String serializedList = prefs.getString(KEY_QUIZ_LIST, null);
        if (serializedList != null) {
            return deserializeList(serializedList);
        } else {
            return new ArrayList<>();  // Return an empty list if no data is found
        }
    }

    private List<String> deserializeList(String serializedList) {
        List<String> list = new ArrayList<>();
        String[] items = serializedList.split(",");
        for (String item : items) {
            list.add(item);
        }
        return list;
    }
}