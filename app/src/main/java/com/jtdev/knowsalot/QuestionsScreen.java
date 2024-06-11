package com.jtdev.knowsalot;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jtdev.knowsalot.Adapters.QuestionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuestionsScreen extends AppCompatActivity {

    RecyclerView recyclerView;
    QuestionsAdapter adapter;
    Button addquestion, backbutton;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_questions_screen);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("questions");

        addquestion = findViewById(R.id.addQuestion);
        backbutton = findViewById(R.id.backBtn);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_questions_layout);

        if (dialog.getWindow()!= null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
        }

        recyclerView = findViewById(R.id.questionRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new QuestionsAdapter(getData(), getQuestionKeys());
        recyclerView.setAdapter(adapter);

// masesave sa firebazse
        addquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display dialog for adding a question
                dialog.show();

                // Handle adding question to Firebase
                Button addBtn = dialog.findViewById(R.id.addBtn);
                EditText questionText = dialog.findViewById(R.id.questionsText);
                EditText choice1Text = dialog.findViewById(R.id.choice1Text);
                EditText choice2Text = dialog.findViewById(R.id.choice2Text);
                EditText choice3Text = dialog.findViewById(R.id.choice3Text);
                EditText choice4Text = dialog.findViewById(R.id.choice4Text);
                EditText rightAnswerText = dialog.findViewById(R.id.rightAnswerText);

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String question = questionText.getText().toString().trim();
                        String choice1 = choice1Text.getText().toString().trim();
                        String choice2 = choice2Text.getText().toString().trim();
                        String choice3 = choice3Text.getText().toString().trim();
                        String choice4 = choice4Text.getText().toString().trim();
                        String rightAnswer = rightAnswerText.getText().toString().trim();

                        if (!question.isEmpty() && !choice1.isEmpty() && !choice2.isEmpty() && !choice3.isEmpty() && !choice4.isEmpty() && !rightAnswer.isEmpty()) {
                            // Push the question with choices and right answer to Firebase
                            DatabaseReference newQuestionRef = myRef.push();
                            newQuestionRef.child("question").setValue(question);
                            newQuestionRef.child("choices").child("choice1").setValue(choice1);
                            newQuestionRef.child("choices").child("choice2").setValue(choice2);
                            newQuestionRef.child("choices").child("choice3").setValue(choice3);
                            newQuestionRef.child("choices").child("choice4").setValue(choice4);
                            newQuestionRef.child("right_answer").setValue(rightAnswer);
                            dialog.dismiss();
                        } else {
                            // Handle empty fields case
                            Toast.makeText(QuestionsScreen.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

//back btn
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(QuestionsScreen.this,CustomQuizScreen.class);
                startActivity(intent);
            }
        });
    }


    // magdidisplay to pag walang internet
    private List<String> getData() {
        List<String> data = new ArrayList<>();
        data.add("Question 1");
        data.add("Question 2");
        data.add("Question 3");
        data.add("Question 4");
        data.add("Question 5");
        data.add("Question 6");
        data.add("Question 7");
        data.add("Question 8");
        return data;
    }

    private List<String> getQuestionKeys() {
        List<String> keys = new ArrayList<>();
        // Populate the keys list with appropriate values
        keys.add("key1");
        keys.add("key2");
        keys.add("key3");
        keys.add("key4");
        keys.add("key5");
        keys.add("key6");
        keys.add("key7");
        keys.add("key8");
        return keys;
    }

    

}