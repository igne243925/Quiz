package com.jtdev.knowsalot.Adapters;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtdev.knowsalot.R;

import org.w3c.dom.Text;

import java.util.List;

public class UserScoreAdapter extends RecyclerView.Adapter<UserScoreAdapter.MyViewHolder> {

    List<Pair<String, Integer>> userdata;

    public UserScoreAdapter(List<Pair<String, Integer>>data) {
        this.userdata= data;
    }

    @NonNull
    @Override
    public UserScoreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_score_layout,parent,false);
        return new UserScoreAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Pair<String, Integer> item = userdata.get(position);
        holder.text1.setText(item.first);
        holder.text2.setText(String.valueOf(item.second));
    }

    @Override
    public int getItemCount() {
        return userdata.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text1;
        TextView text2;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            text1 = itemView.findViewById(R.id.subjects);
            text2 = itemView.findViewById(R.id.userScore);
        }
    }
}
