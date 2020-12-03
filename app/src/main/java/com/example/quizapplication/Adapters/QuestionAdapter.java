package com.example.quizapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapplication.Fragments.QuestionList;
import com.example.quizapplication.R;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {

    QuestionList questionList;
    Context context;
    int size;

    public QuestionAdapter(QuestionList questionList, Context context, int size) {
        this.questionList = questionList;
        this.context = context;
        this.size = size;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz2,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(String.valueOf(position+1));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionList.questionListener.displayQuestion(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.quiz_numberr);
        }
    }
}
