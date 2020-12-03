package com.example.quizapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapplication.QuizActivity;
import com.example.quizapplication.R;

import java.util.ArrayList;
import java.util.Random;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> totalQuiz;

    public QuizAdapter(Context context, ArrayList<String> totalQuiz) {
        this.context = context;
        this.totalQuiz = totalQuiz;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name=totalQuiz.get(position).replace(".json","");
        holder.textView1.setText(String.valueOf(position));
        holder.textView2.setText(name);
        Random rnd=new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        //holder.textView.setBackgroundColor(color);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, QuizActivity.class);
                intent.putExtra("Quiz_number",position);
                intent.putExtra("Quiz_title",name);
                context.startActivity(intent);

                ((FragmentActivity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return totalQuiz.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView1,textView2;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.quiz_number);
            textView2=itemView.findViewById(R.id.quizName);

        }
    }
}
