package com.example.quizapplication.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.quizapplication.Adapters.QuestionCardAdapter;
import com.example.quizapplication.Questions;
import com.example.quizapplication.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

import static android.content.ContentValues.TAG;


public class QuestionFragment extends Fragment {


    ArrayList<Questions> questionsList=new ArrayList<>();

    public QuestionFragment() {

    }

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_question, container, false);

        recyclerView=view.findViewById(R.id.showQuestions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        QuestionCardAdapter questionCardAdapter=new QuestionCardAdapter(questionsList, getContext());

        recyclerView.setAdapter(questionCardAdapter);
        Collections.shuffle(questionsList);
        questionCardAdapter.notifyDataSetChanged();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {

        super.onStart();
        String json = loadJsonString("questions.json");

        try {
            JSONObject jsonObject=new JSONObject(json);
            JSONArray jsonArray=jsonObject.getJSONArray("Questions");

            for(int i=0;i<jsonArray.length();i++)
            {
                Questions questionTemp=new Questions();
                JSONObject Question=jsonArray.getJSONObject(i);
                JSONArray options=Question.getJSONArray("options");
                JSONArray correct=Question.getJSONArray("correct");
                JSONArray topics=Question.getJSONArray("topic");
                String[] option=new String[options.length()];
                String[] answers=new String[correct.length()];
                String[] topic=new String[topics.length()];
                for(int j=0;j<options.length();j++)
                {
                    option[j]=options.getString(j);
                }
                for(int j=0;j<correct.length();j++)
                {
                    answers[j]=correct.getString(j);
                }
                for(int j=0;j<topics.length();j++)
                {
                    topic[j]=topics.getString(j);
                }
                questionTemp.setType(Question.getInt("type"));
                questionTemp.setOptions(option);
                //Toast.makeText(this, Question.getString("question"), Toast.LENGTH_SHORT).show();
                questionTemp.setQuestion(Question.getString("question"));
                questionTemp.setLastSolved(Question.getBoolean("lastSolved"));
                questionTemp.setCorrect(answers);
                questionTemp.setTopic(topic);
                questionsList.add(questionTemp);
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "onStart: "+e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String loadJsonString(String s) {


        String json = null;
        try
        {
            InputStream inputStream = getContext().getAssets().open(s);
            int size=inputStream.available();
            byte[] buffer=new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return json;
    }
}