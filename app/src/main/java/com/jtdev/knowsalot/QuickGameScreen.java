package com.jtdev.knowsalot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuickGameScreen extends AppCompatActivity {

    CardView mathCard, englishCard, scienceCard, historyCard, peCard, musicCard;
    Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_game_screen);

        mathCard = findViewById(R.id.math);
        englishCard = findViewById(R.id.english);
        scienceCard = findViewById(R.id.science);
        historyCard = findViewById(R.id.history);
        peCard = findViewById(R.id.pe);
        musicCard = findViewById(R.id.music);
        backButton = findViewById(R.id.backBtn);

   //back btn
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuickGameScreen.this,PlayerScreen.class);
                startActivity(intent);
            }
        });
// eto yung sa pagclinick yung question pagclinick math dun sa math siya sasagot
        mathCard.setOnClickListener(view -> startQuiz("Math"));
        englishCard.setOnClickListener(view -> startQuiz("English"));
        scienceCard.setOnClickListener(view -> startQuiz("Science"));
        historyCard.setOnClickListener(view -> startQuiz("History"));
        peCard.setOnClickListener(view -> startQuiz("PE"));
        musicCard.setOnClickListener(view -> startQuiz("Music"));
    }

    private void startQuiz(String subject) {
        Intent intent = new Intent(QuickGameScreen.this, QuizScreen.class);
        intent.putExtra("SUBJECT", subject);
        startActivity(intent);
    }
}