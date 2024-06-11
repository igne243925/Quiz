package com.jtdev.knowsalot.Adapters;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jtdev.knowsalot.R;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.MyViewHolder> {

    List<String> question;
    List<String> questionKeys;
    DatabaseReference myRef;

    public QuestionsAdapter(List<String> questions, List<String> questionKeys) {
        this.question = questions;
        this.questionKeys = questionKeys;
        myRef = FirebaseDatabase.getInstance().getReference("questions");

        // Add a listener to update the adapter data when Firebase data changes
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                question.clear();
                questionKeys.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    String value = snapshot.child("question").getValue(String.class); // Fetch only the question
                    question.add(value);
                    questionKeys.add(key); // Add corresponding key
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("QuestionsAdapter", "Failed to read value.", error.toException());
            }
        });
    }

    @NonNull
    @Override
    public QuestionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String questionText = question.get(position);
        String questionKey = questionKeys.get(position);

        holder.questionText.setText(questionText);

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Edit button clicked for question: " + questionText);
                showEditDialog(v, questionText, questionKey); // Pass questionText and questionKey
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYesNoDialog(v, questionText, questionKey);
            }
        });
    }

    private void showEditDialog(View view, String questionText, String questionKey) {
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.add_questions_layout);

        EditText questionEditText = dialog.findViewById(R.id.questionsText);
        EditText choice1EditText = dialog.findViewById(R.id.choice1Text);
        EditText choice2EditText = dialog.findViewById(R.id.choice2Text);
        EditText choice3EditText = dialog.findViewById(R.id.choice3Text);
        EditText choice4EditText = dialog.findViewById(R.id.choice4Text);
        EditText rightAnswerEditText = dialog.findViewById(R.id.rightAnswerText);

        questionEditText.setText(questionText); // Set the current question text to EditText

        Button editButton = dialog.findViewById(R.id.addBtn);
        editButton.setText("Save"); // Change the text of the button to 'Save'

        // Fetch existing choices and right answer from Firebase and set them to EditText
        DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference("questions").child(questionKey);
        questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String choice1 = dataSnapshot.child("choices").child("choice1").getValue(String.class);
                    String choice2 = dataSnapshot.child("choices").child("choice2").getValue(String.class);
                    String choice3 = dataSnapshot.child("choices").child("choice3").getValue(String.class);
                    String choice4 = dataSnapshot.child("choices").child("choice4").getValue(String.class);
                    String rightAnswer = dataSnapshot.child("right_answer").getValue(String.class);

                    choice1EditText.setText(choice1);
                    choice2EditText.setText(choice2);
                    choice3EditText.setText(choice3);
                    choice4EditText.setText(choice4);
                    rightAnswerEditText.setText(rightAnswer);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedQuestion = questionEditText.getText().toString().trim();
                String editedChoice1 = choice1EditText.getText().toString().trim();
                String editedChoice2 = choice2EditText.getText().toString().trim();
                String editedChoice3 = choice3EditText.getText().toString().trim();
                String editedChoice4 = choice4EditText.getText().toString().trim();
                String editedRightAnswer = rightAnswerEditText.getText().toString().trim();

                if (!editedQuestion.isEmpty() && !editedChoice1.isEmpty() && !editedChoice2.isEmpty() &&
                        !editedChoice3.isEmpty() && !editedChoice4.isEmpty() && !editedRightAnswer.isEmpty()) {
                    // Update the question, choices, and right answer in Firebase
                    questionRef.child("question").setValue(editedQuestion);
                    questionRef.child("choices").child("choice1").setValue(editedChoice1);
                    questionRef.child("choices").child("choice2").setValue(editedChoice2);
                    questionRef.child("choices").child("choice3").setValue(editedChoice3);
                    questionRef.child("choices").child("choice4").setValue(editedChoice4);
                    questionRef.child("right_answer").setValue(editedRightAnswer);
                    dialog.dismiss();
                } else {
                    // Handle empty fields case
                    Toast.makeText(view.getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
        }
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return question.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView questionText;
        Button editBtn, deleteBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }



    private void showYesNoDialog(View view, String questionText, String questionKey) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete this question?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the question from Firebase
                        DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference("questions").child(questionKey);
                        questionRef.removeValue();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
