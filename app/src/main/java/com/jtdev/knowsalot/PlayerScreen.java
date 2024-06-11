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

public class PlayerScreen extends AppCompatActivity {

    Button quickbutton,custombutton,profilebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_screen);

        quickbutton = findViewById(R.id.quickBtn);
        custombutton = findViewById(R.id.customBtn);
        profilebutton = findViewById(R.id.menuBtn);

// Quickgame btn that proceed to the quick game
        quickbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerScreen.this, QuickGameScreen.class);
                startActivity(intent);
            }
        });


  // it wll proceed to the created questionaire
        custombutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerScreen.this, StudentQuizzesScreen .class);
                startActivity(intent);
            }
        });
// profile overview
        profilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerScreen.this, PlayerProfileScreen.class);
                startActivity(intent);
            }
        });
    }
}