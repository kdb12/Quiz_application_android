package com.example.quizapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

public class StartActivity extends AppCompatActivity {

    MaterialEditText etName,etCollege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        etName=findViewById(R.id.tvName);
        etCollege=findViewById(R.id.tvCollege);


    }

    public void clicked(View view)
    {
        String name, institute, registrationDate;
        LinkedHashMap<String, Integer> dailyQuestionTally=new LinkedHashMap<>();
        ArrayList<TopicWise> topicWiseTally=new ArrayList<>();
        LinkedHashMap<String, Integer> quizzScores=new LinkedHashMap<>();


       if(TextUtils.isEmpty(etName.getText().toString()) || TextUtils.isEmpty(etCollege.getText().toString()))
       {
           Toast.makeText(this, "CREDENTIALS EMPTY PLEASE TRY AGAIN!!", Toast.LENGTH_SHORT).show();
       }
       else if(etName.getText().toString().length() < 2 || etCollege.getText().toString().length() < 2)
       {
           Toast.makeText(this, "INVALID CREDENTIALS PLEASE TRY AGAIN!!", Toast.LENGTH_SHORT).show();
       }
       else
       {


            name=etName.getText().toString();
            institute=etCollege.getText().toString();
            registrationDate=User.getCurrentDate();

            User user=new User(name,institute,registrationDate,dailyQuestionTally,topicWiseTally,quizzScores);

            String userJson=new Gson().toJson(user);

            getSharedPreferences("QuizAppPreferences",MODE_PRIVATE).edit().putString("userInfo",userJson).apply();

           startActivity(new Intent(this,MainActivity.class));
           finish();
       }


    }

    @Override
    protected void onStart() {
        String s=getSharedPreferences("QuizAppPreferences",MODE_PRIVATE).getString("userInfo",null);

        if(s==null)
        {
            super.onStart();
        }
        else
        {
            super.onStart();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }
}