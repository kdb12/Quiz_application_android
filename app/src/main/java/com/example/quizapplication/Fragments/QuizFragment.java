package com.example.quizapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizapplication.Adapters.QuizAdapter;
import com.example.quizapplication.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class QuizFragment extends Fragment {



    public QuizFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    ArrayList<String> totalQuiz;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_quiz, container, false);

        recyclerView=view.findViewById(R.id.quiz_recycle);
        totalQuiz=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        loadQuizes("quizes");

        recyclerView.setAdapter(new QuizAdapter(getContext(),totalQuiz));





        return view;
    }

    private void loadQuizes(String path) {
        String [] list;
        try {
            list = getContext().getAssets().list(path);
            if (list.length > 0) {


                for(String P:list)
                {
                    totalQuiz.add(P);
                    //p="quiz3.json"
                }

            }
        } catch (IOException e) {

        }

    }
}