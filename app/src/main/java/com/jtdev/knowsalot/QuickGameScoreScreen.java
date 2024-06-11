package com.jtdev.knowsalot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuickGameScoreScreen extends AppCompatActivity {

    Button retrybutton,homebutton;
    private TextView scoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_game_score_screen);


        retrybutton = findViewById(R.id.retryBtn);
        homebutton = findViewById(R.id.homeBtn);
        scoreText = findViewById(R.id.scoreText);


// score sa quick game
        int score = getIntent().getIntExtra("SCORE", 0);
        int totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 1);
        String subject = getIntent().getStringExtra("SUBJECT"); // Getting the subject from the intent

        scoreText.setText(score + " / " + totalQuestions);


  // reretry to
        retrybutton.setOnClickListener(view -> {
            Intent intent = new Intent(QuickGameScoreScreen.this, QuizScreen.class);
            intent.putExtra("SUBJECT", subject); // Pass the subject back
            startActivity(intent);
            finish(); // Close the current activity to avoid stacking
        });
// pag ayaw na
        homebutton.setOnClickListener(view -> {
            Intent intent = new Intent(QuickGameScoreScreen.this, QuickGameScreen.class);
            startActivity(intent);
            finish(); // Close the current activity
        });
    }
}