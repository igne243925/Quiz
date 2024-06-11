package com.jtdev.knowsalot;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jtdev.knowsalot.Adapters.UsernameAdapter;

import java.util.ArrayList;
import java.util.List;

public class ScoreScreen extends AppCompatActivity {

    RecyclerView recyclerView;
    UsernameAdapter adapter;
    Button backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);

        backbutton = findViewById(R.id.returnBtn);
        recyclerView = findViewById(R.id.usernameRecycler);


        adapter = new UsernameAdapter(getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

   // yung score sa custom game na nasave sa firebase eto yun dito kukunin para madisplay sa teacher/ scores
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("quiz_scores")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> scoreList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = document.getString("username");
                            int score = document.getLong("score").intValue();
                            scoreList.add("Username: " + username + ", Score: " + score);
                        }
                        // Update RecyclerView with scoreList
                        adapter = new UsernameAdapter(scoreList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Log.e(TAG, "Error getting scores", task.getException());
                    }
                });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreScreen.this,AdminScreen.class);
                startActivity(intent);
            }
        });
    }
    private List<String> getData() {

        List<String> data = new ArrayList<>();
        data.add("Username 1");
        data.add("Username 2");
        data.add("Username 3");
        data.add("Username 4");
        data.add("Username 5");
        data.add("Username 6");
        data.add("Username 7");
        data.add("Username 8");
        return data;
    }
}