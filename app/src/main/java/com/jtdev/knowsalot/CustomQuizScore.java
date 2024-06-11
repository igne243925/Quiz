package com.jtdev.knowsalot;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CustomQuizScore extends AppCompatActivity {

    Button homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_quiz_score);

        homebutton = findViewById(R.id.homeBtn);

        int score = getIntent().getIntExtra("SCORE", 0);
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("" + score +"");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Retrieve username from Firestore
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String username = document.getString("username");
                                saveScoreToFirestore(username, score);
                            } else {
                                Toast.makeText(CustomQuizScore.this, "No user data found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CustomQuizScore.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

// Home btn
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomQuizScore.this, PlayerScreen.class);
                startActivity(intent);
            }
        });
    }


 // save scores to firebase
    private void saveScoreToFirestore(String username, int score) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> scoreData = new HashMap<>();
        scoreData.put("username", username);
        scoreData.put("score", score);
        firestore.collection("quiz_scores")
                .document(username) // Using username as document ID
                .set(scoreData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Score successfully saved to Firestore"))
                .addOnFailureListener(e -> Log.e(TAG, "Error saving score to Firestore", e));
    }
}