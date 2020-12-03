package com.example.quizapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapplication.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    String names[];
    String rollNo[];

    public MyAdapter()
    {
        names=new String[3];
        rollNo=new String[3];

        names[0]="KRISHNA BHUTADA";
        names[1]="SOURAV BATGIRI";
        names[2]="PRATIK BHONJANE";

        rollNo[0]="31212";
        rollNo[1]="31209";
        rollNo[2]="31211";

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(names[position]);
        holder.rollNumber.setText(rollNo[position]);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,rollNumber;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            rollNumber=itemView.findViewById(R.id.roll_no);
        }
    }
}
