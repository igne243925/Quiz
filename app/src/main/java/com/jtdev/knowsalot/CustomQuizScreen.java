package com.jtdev.knowsalot;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jtdev.knowsalot.Adapters.QuizzesAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomQuizScreen extends AppCompatActivity {

    RecyclerView recyclerView;
    QuizzesAdapter adapter;
    Button addbutton,backbutton,addquiz;
    EditText quizname;
    Dialog dialog;
    int spanCount = 2;
    private List<String> quizList;

    private static final String PREF_NAME = "QuizListPrefs";
    private static final String KEY_QUIZ_LIST = "quizList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_quiz_screen);

        quizList = getData();  // Initialize the list with default data

        addbutton = findViewById(R.id.addBtn);
        backbutton = findViewById(R.id.returnBtn);

        setupDialog();  // Setup dialog for adding quizzes

        recyclerView = findViewById(R.id.quizRecycler);
        adapter = new QuizzesAdapter(quizList);  // Pass the list to the adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));



// add button to input the addquiz
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();  // Show dialog to add a new quiz
            }
        });

        addquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quizName = quizname.getText().toString().trim();  // Get the quiz name
                if (!quizName.isEmpty()) {  // Check if it's not empty
                    quizList.add(quizName);  // Add to the list
                    adapter.notifyDataSetChanged();  // Notify adapter about the change
                    saveQuizListToPrefs();  // Save the updated list to SharedPreferences
                    dialog.dismiss();  // Close the dialog
                } else {
                    Toast.makeText(CustomQuizScreen.this, "Please enter a quiz name", Toast.LENGTH_SHORT).show();  // Show an error message
                }
            }
        });

// backbtn to admin screen

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuizListToPrefs();  // Ensure the list is saved before going back
                Intent intent = new Intent(CustomQuizScreen.this, AdminScreen.class);
                startActivity(intent);
            }
        });
    }

 // Add quiz layout (put the quiz name)
    private void setupDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_quiz_layout);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);  // Allow dialog to be dismissed by clicking outside
        }

        addquiz = dialog.findViewById(R.id.addQuizBtn);  // Button to add the quiz
        quizname = dialog.findViewById(R.id.quizNames);  // EditText for the quiz name
    }

    // List of the quiz created
    private List<String> getData() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String serializedList = prefs.getString(KEY_QUIZ_LIST, null);
        if (serializedList != null) {
            return deserializeList(serializedList);
        } else {
            return new ArrayList<>();  // Return an empty list if no data is found
        }
    }

    private void saveQuizListToPrefs() {
        String serializedList = serializeList(quizList);
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_QUIZ_LIST, serializedList);
        editor.apply();
    }

    private String serializeList(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String item : list) {
            builder.append(item).append(",");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);  // Remove the trailing comma
        }
        return builder.toString();
    }

    private List<String> deserializeList(String serializedList) {
        List<String> list = new ArrayList<>();
        if (!serializedList.isEmpty()) {
            String[] items = serializedList.split(",");
            for (String item : items) {
                list.add(item);
            }
        }
        return list;
    }

}
