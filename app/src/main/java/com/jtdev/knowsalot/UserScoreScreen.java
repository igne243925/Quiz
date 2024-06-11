package com.jtdev.knowsalot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jtdev.knowsalot.Adapters.UserScoreAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserScoreScreen extends AppCompatActivity {

    RecyclerView recyclerView;
    UserScoreAdapter adapter;
    Button backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_score_screen);


        backbutton = findViewById(R.id.returnBtn);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserScoreScreen.this,ScoreScreen.class);
                startActivity(intent);
            }
        });


        List<Pair<String, Integer>> sampleData = new ArrayList<>();
        sampleData.add(new Pair<>("English", 20));
        sampleData.add(new Pair<>("Math", 15));
        sampleData.add(new Pair<>("Science", 10));
        sampleData.add(new Pair<>("History", 5));

        recyclerView = findViewById(R.id.scoreRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserScoreAdapter(sampleData);
        recyclerView.setAdapter(adapter);
    }
}