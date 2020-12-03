package com.example.quizapplication.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizapplication.Adapters.QuestionAdapter;
import com.example.quizapplication.R;


public class QuestionList extends Fragment {


    private RecyclerView recyclerView;
    public QuestionListener questionListener;

    public QuestionList() {

    }

    public interface QuestionListener
    {
        public void displayQuestion(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        recyclerView = view.findViewById(R.id.question_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new QuestionAdapter(this,getContext(),getContext().getSharedPreferences("totalQuestions", Context.MODE_PRIVATE).getInt("size",1)));

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;

        try {
            questionListener=(QuestionListener) activity;

        }
        catch(ClassCastException e)
        {
            e.getMessage();
        }
    }
}