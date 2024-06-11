package com.jtdev.knowsalot.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jtdev.knowsalot.CustomQuizScreen;
import com.jtdev.knowsalot.QuestionsScreen;
import com.jtdev.knowsalot.R;

import java.util.List;

public class QuizzesAdapter extends RecyclerView.Adapter<QuizzesAdapter.MyViewHolder>{

    List<String> quizzes;

    private void deleteItem(int position) {
        quizzes.remove(position);  // Remove quiz from the list
        notifyDataSetChanged();  // Notify adapter to refresh
        // Optionally, you can save the updated list to SharedPreferences or any other storage mechanism.
    }

    private void editItem(int position, String newName) {
        quizzes.set(position, newName);  // Update quiz name
        notifyDataSetChanged();  // Refresh the RecyclerView
        // Optionally, you can save the updated list to SharedPreferences or any other storage mechanism.
    }

    public QuizzesAdapter(List<String> data) {
        this.quizzes = data;
    }
    @NonNull
    @Override
    public QuizzesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quizzes_layout,parent,false);
        return new QuizzesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizzesAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String item = quizzes.get(position);
        holder.Text.setText(item);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QuestionsScreen.class);
                v.getContext().startActivity(intent);
            }
        });

        // Fix: Pass the position to showPopupMenu
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, position);  // Pass the position here
            }
        });
    }

    private void showPopupMenu(View v, int position) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.popup_edit) {
                    showEditDialog(v, position);  // Pass position to edit dialog
                    return true;
                } else if (item.getItemId() == R.id.popup_delete) {
                    showYesNoDialog(v, position);  // Pass position to delete dialog
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    private void showEditDialog(View view, int position) {
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.add_quiz_layout);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
        }

        EditText quizNameEditText = dialog.findViewById(R.id.quizNames);
        Button addQuizButton = dialog.findViewById(R.id.addQuizBtn);

        quizNameEditText.setText(quizzes.get(position));  // Set existing quiz name for editing

        addQuizButton.setOnClickListener(v -> {
            String newQuizName = quizNameEditText.getText().toString().trim();
            if (!newQuizName.isEmpty()) {
                editItem(position, newQuizName);  // Edit the quiz name
                dialog.dismiss();
            } else {
                Toast.makeText(view.getContext(), "Quiz name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }



    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Text;
        CardView cardView;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Text = itemView.findViewById(R.id.quizName);
            cardView = itemView.findViewById(R.id.card);
            imageView = itemView.findViewById(R.id.menu);
        }
    }

    private void showPopupDialog(View view) {
        View popupView = LayoutInflater.from(view.getContext()).inflate(R.layout.add_quiz_layout, null);

        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.add_quiz_layout);

        if (dialog.getWindow()!= null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);

        }
        dialog.show();
    }

    private void showYesNoDialog(View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Delete Quiz")
                .setMessage("Are you sure you want to delete this quiz?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(position);  // Permanently delete the quiz
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();  // Dismiss the dialog if the user cancels
                    }
                })
                .show();
    }
}
