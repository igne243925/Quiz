package com.jtdev.knowsalot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.color.utilities.Score;

public class AdminScreen extends AppCompatActivity {

    Button create,scores,profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        create = findViewById(R.id.createBtn);
        scores = findViewById(R.id.scoresBtn);
        profile = findViewById(R.id.menuBtn);

 // Create quiz
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminScreen.this,CustomQuizScreen.class);
                startActivity(intent);
            }
        });


// Check the scores of the student takers
        scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminScreen.this, ScoreScreen.class);
                startActivity(intent);
            }
        });

  // Go to Profile overview
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminScreen.this, AdminIProfileScreen.class);
                startActivity(intent);
            }
        });
    }
}