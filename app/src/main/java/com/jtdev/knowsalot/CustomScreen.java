package com.jtdev.knowsalot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomScreen extends AppCompatActivity {

    TextView questionText;
    Button backbutton, submitbutton, choice1, choice2, choice3, choice4;
    private DatabaseReference databaseReference;
    private ValueEventListener questionListener;
    private List<DataSnapshot> questionSnapshots;
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_game_quiz_screen);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("questions");

        questionText = findViewById(R.id.questionText);
        backbutton = findViewById(R.id.backBtn);
        submitbutton = findViewById(R.id.submitBtn);
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choice3 = findViewById(R.id.choice3);
        choice4 = findViewById(R.id.choice4);

        loadQuestions();

// choices btn if the user tap one of them it will show toast that the user choose that choice
        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a toast indicating choice 1 is selected
                showToast("Choice 1 selected: " + choice1.getText().toString());
                if (isCorrectAnswer(1)) {
                    showToast("Correct Answer!");
                    score++; // Increment the score
                } else {
                    showToast("Wrong Answer!");
                }
            }
        });

        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a toast indicating choice 2 is selected
                showToast("Choice 2 selected: " + choice2.getText().toString());
                if (isCorrectAnswer(2)) {
                    showToast("Correct Answer!");
                    score++; // Increment the score
                } else {
                    showToast("Wrong Answer!");
                }
            }
        });

        choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a toast indicating choice 3 is selected
                showToast("Choice 3 selected: " + choice3.getText().toString());
                if (isCorrectAnswer(3)) {
                    showToast("Correct Answer!");
                    score++; // Increment the score
                } else {
                    showToast("Wrong Answer!");
                }
            }
        });

        choice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a toast indicating choice 4 is selected
                showToast("Choice 4 selected: " + choice4.getText().toString());
                if (isCorrectAnswer(4)) {
                    showToast("Correct Answer!");
                    score++; // Increment the score
                } else {
                    showToast("Wrong Answer!");
                }
            }
        });

  //back btn
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomScreen.this, PlayerScreen.class);
                startActivity(intent);
            }
        });

// submit btn if there is question it will proceed to the next questions else it will display scores
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex < questionSnapshots.size() - 1) {
                    // If there are more questions, load the next question
                    currentQuestionIndex++;
                    displayQuestion(currentQuestionIndex);
                } else {
                    // If all questions have been answered, show the score
                    Intent intent = new Intent(CustomScreen.this, CustomQuizScore.class);
                    // Pass the score to the CustomQuizScore activity
                    intent.putExtra("SCORE", score);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean isCorrectAnswer(int choiceNumber) {
        DataSnapshot questionSnapshot = questionSnapshots.get(currentQuestionIndex);
        String correctAnswer = questionSnapshot.child("right_answer").getValue(String.class);
        String selectedChoice = null;
        switch (choiceNumber) {
            case 1:
                selectedChoice = choice1.getText().toString();
                break;
            case 2:
                selectedChoice = choice2.getText().toString();
                break;
            case 3:
                selectedChoice = choice3.getText().toString();
                break;
            case 4:
                selectedChoice = choice4.getText().toString();
                break;
        }
        return correctAnswer != null && correctAnswer.equals(selectedChoice);
    }


// fetch questions that the user created so that the user can answer it
    private void loadQuestions() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questionSnapshots = new ArrayList<>();
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    questionSnapshots.add(questionSnapshot);
                }
                // Display the first question
                displayQuestion(currentQuestionIndex);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CustomScreen.this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // it will display questions
    private void displayQuestion(int index) {
        DataSnapshot questionSnapshot = questionSnapshots.get(index);
        String question = questionSnapshot.child("question").getValue(String.class);
        String choice1Text = questionSnapshot.child("choices").child("choice1").getValue(String.class);
        String choice2Text = questionSnapshot.child("choices").child("choice2").getValue(String.class);
        String choice3Text = questionSnapshot.child("choices").child("choice3").getValue(String.class);
        String choice4Text = questionSnapshot.child("choices").child("choice4").getValue(String.class);

        // Update UI with question and choices
        questionText.setText(question);
        choice1.setText(choice1Text);
        choice2.setText(choice2Text);
        choice3.setText(choice3Text);
        choice4.setText(choice4Text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the ValueEventListener when activity is destroyed to prevent memory leaks
        if (databaseReference != null && questionListener != null) {
            databaseReference.removeEventListener(questionListener);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}