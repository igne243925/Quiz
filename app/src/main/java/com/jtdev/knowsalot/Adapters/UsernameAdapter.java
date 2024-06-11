package com.jtdev.knowsalot.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jtdev.knowsalot.R;
import com.jtdev.knowsalot.UserScoreScreen;

import java.util.List;

public class UsernameAdapter extends RecyclerView.Adapter<UsernameAdapter.MyViewHolder> {

    List<String> usernames;

    public UsernameAdapter(List<String> data) {
        this.usernames = data;
    }

    @NonNull
    @Override
    public UsernameAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.username_layout,parent,false);
        return new UsernameAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsernameAdapter.MyViewHolder holder, int position) {
        String item = usernames.get(position);
        holder.text.setText(item);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserScoreScreen.class);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.userNames);
            cardView = itemView.findViewById(R.id.user);
        }
    }
}
