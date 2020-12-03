package com.example.quizapplication.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapplication.Fragments.ShowQuestionFragment;
import com.example.quizapplication.Questions;
import com.example.quizapplication.R;

import java.util.ArrayList;
import java.util.Random;

public class QuestionCardAdapter extends RecyclerView.Adapter<QuestionCardAdapter.MyViewHolder> {

    ArrayList<Questions> questionsList;
    Context context;

    public QuestionCardAdapter(ArrayList<Questions> questionsList, Context context) {
        this.questionsList = questionsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.question_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Questions questions=questionsList.get(position);
        holder.textView.setText(questions.getQuestion());
        Random random = new Random();
        int r=random.nextInt(200),g=random.nextInt(200),b=random.nextInt(200);
        int c= Color.argb(255,r,g,b);

        holder.cardView.setCardBackgroundColor(c);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowQuestionFragment showQuestionFragment=new ShowQuestionFragment();
                Bundle bundle=new Bundle();
                bundle.putParcelable("question",questions);
                showQuestionFragment.setArguments(bundle);

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,showQuestionFragment,null).commit();
                questionsList.remove(questions);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.question_card);
            cardView=itemView.findViewById(R.id.card);
        }
    }
}
