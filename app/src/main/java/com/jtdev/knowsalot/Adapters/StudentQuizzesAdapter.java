package com.jtdev.knowsalot.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jtdev.knowsalot.CustomScreen;
import com.jtdev.knowsalot.QuizScreen;
import com.jtdev.knowsalot.R;

import java.util.List;

public class StudentQuizzesAdapter extends RecyclerView.Adapter<QuizzesAdapter.MyViewHolder>{

    List<String> quizzes;

    public StudentQuizzesAdapter(List<String> data) {
        this.quizzes = data;
    }
    @NonNull
    @Override
    public QuizzesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quizzes_student_layout,parent,false);
        return new QuizzesAdapter.MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull QuizzesAdapter.MyViewHolder holder, int position) {
        String item = quizzes.get(position);
        holder.Text.setText(item);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CustomScreen.class);
                v.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Text;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Text = itemView.findViewById(R.id.quizName);
            cardView = itemView.findViewById(R.id.card);
        }
    }
}
