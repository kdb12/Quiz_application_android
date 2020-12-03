package com.example.quizapplication.Fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.quizapplication.Questions;
import com.example.quizapplication.R;
import com.example.quizapplication.TopicWise;
import com.example.quizapplication.User;
import com.google.android.material.snackbar.Snackbar;


public class ShowQuestionFragment extends Fragment implements View.OnClickListener {


    RadioButton rOption1,rOption2,rOption3,rOption4;
    CheckBox cOption1,cOption2,cOption3,cOption4;
    LinearLayout checkBoxLayout,trueFalseLayout;
    Button btnOk,btnTrue,btnFalse;
    EditText etQuestion;
    RadioGroup radioGroup;
    Questions questions;
    String p="";


    public ShowQuestionFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_show_question, container, false);

        rOption1=view.findViewById(R.id.r1);
        rOption2=view.findViewById(R.id.r2);
        rOption3=view.findViewById(R.id.r3);
        rOption4=view.findViewById(R.id.r4);

        cOption1=view.findViewById(R.id.c1);
        cOption2=view.findViewById(R.id.c2);
        cOption3=view.findViewById(R.id.c3);
        cOption4=view.findViewById(R.id.c4);

        btnFalse=view.findViewById(R.id.false_btn);
        btnTrue=view.findViewById(R.id.true_btn);
        btnOk=view.findViewById(R.id.btnOk);

        checkBoxLayout=view.findViewById(R.id.check_layout);
        trueFalseLayout=view.findViewById(R.id.tf_layout);
        radioGroup=view.findViewById(R.id.group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.r1:p=rOption1.getText().toString();break;
                    case R.id.r2:p=rOption2.getText().toString();break;
                    case R.id.r3:p=rOption3.getText().toString();break;
                    case R.id.r4:p=rOption4.getText().toString();break;
                }

            }
        });

        etQuestion=view.findViewById(R.id.description);
        final Handler handler = new Handler(Looper.getMainLooper());

        Bundle bundle=this.getArguments();

        if(bundle!=null)
        {
            questions=bundle.getParcelable("question");
        }

        clearInput();
        loadQuestion();
        btnTrue.setOnClickListener(this);
        btnFalse.setOnClickListener(this);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(questions.getType()==1)
                {
                    String r=questions.getCorrect()[0];

                    Snackbar snackbar;
                    if(r.equalsIgnoreCase(p))
                    {
                        snackbar = Snackbar.make(v, "CORRECT ANSWER !!!", Snackbar.LENGTH_LONG);

                        correctSolved();


                    }
                    else
                    {
                        snackbar = Snackbar.make(v, "WRONG ANSWER !!! \n CORRECT ANSWER ="+r, Snackbar.LENGTH_LONG);

                        wrongSolved();
                    }
                    snackbar.show();






                }
                else if(questions.getType()==2)
                {
                    if(cOption1.isChecked())
                    {
                        p+=cOption1.getText().toString();
                    }
                    if(cOption1.isChecked())
                    {
                        p+=cOption1.getText().toString();
                    }
                    if(cOption1.isChecked())
                    {
                        p+=cOption1.getText().toString();
                    }
                    if(cOption1.isChecked())
                    {
                        p+=cOption1.getText().toString();
                    }

                    String r="";

                    for(int i=0;i<questions.getCorrect().length;i++)
                    {
                        r+=questions.getCorrect()[i];
                    }

                    Snackbar snackbar;
                    if(r.equalsIgnoreCase(p))
                    {
                        snackbar = Snackbar.make(v, "CORRECT ANSWER !!!", Snackbar.LENGTH_LONG);
                        correctSolved();
                    }
                    else
                    {
                        snackbar = Snackbar.make(v, "WRONG ANSWER !!! \n CORRECT ANSWER ="+r, Snackbar.LENGTH_LONG);
                        wrongSolved();
                    }
                    snackbar.show();



                }
                else
                {
                    String r=questions.getCorrect()[0];

                    Snackbar snackbar;
                    if(r.equalsIgnoreCase(p))
                    {
                        snackbar = Snackbar.make(v, "CORRECT ANSWER !!!", Snackbar.LENGTH_LONG);
                        correctSolved();
                    }
                    else
                    {
                        snackbar = Snackbar.make(v, "WRONG ANSWER !!! \n CORRECT ANSWER ="+r, Snackbar.LENGTH_LONG);
                        wrongSolved();
                    }
                    snackbar.show();



                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       getActivity().onBackPressed();
                    }
                }, 3000);
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void wrongSolved() {
        User user= User.getUser(getContext());
        String td=User.getCurrentDate();
        if(user.getDailyQuestionTally().containsKey(td))
        {
            int currentTally=user.getDailyQuestionTally().get(td);
            user.getDailyQuestionTally().replace(td,currentTally+1);
        }
        else
        {
            user.getDailyQuestionTally().put(td,1);
        }

        String cTopic=questions.getTopic()[0];

        boolean flag=true;

        for(TopicWise topic:user.getTopicWiseTally())
        {
            if(topic.getTopicName().equalsIgnoreCase(cTopic))
            {
                flag=false;
                topic.setWrong(topic.getWrong()+1);
            }

        }

        if(flag)
        {
            user.getTopicWiseTally().add(new TopicWise(cTopic,0,1));
        }
        User.updateUser(getContext(),user);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void correctSolved() {
        User user= User.getUser(getContext());
        String td=User.getCurrentDate();
        if(user.getDailyQuestionTally().containsKey(td))
        {
            int currentTally=user.getDailyQuestionTally().get(td);
            user.getDailyQuestionTally().replace(td,currentTally+1);
        }
        else
        {
            user.getDailyQuestionTally().put(td,1);
        }
        String cTopic=questions.getTopic()[0];

        boolean flag=true;

        for(TopicWise topic:user.getTopicWiseTally())
        {
            if(topic.getTopicName().equalsIgnoreCase(cTopic))
            {
                flag=false;
                topic.setCorrect(topic.getCorrect()+1);
            }

        }

        if(flag)
        {
            user.getTopicWiseTally().add(new TopicWise(cTopic,1,0));
        }

        User.updateUser(getContext(),user);

    }

    private void loadQuestion()
    {
        etQuestion.setText(questions.getQuestion());

        if(questions.getType()==1)
        {
            radioGroup.setVisibility(View.VISIBLE);
            checkBoxLayout.setVisibility(View.GONE);
            trueFalseLayout.setVisibility(View.GONE);

            rOption1.setText(questions.getOptions()[0]);
            rOption2.setText(questions.getOptions()[1]);
            rOption3.setText(questions.getOptions()[2]);
            rOption4.setText(questions.getOptions()[3]);

        }
        else if(questions.getType()==2)
        {
            radioGroup.setVisibility(View.GONE);
            checkBoxLayout.setVisibility(View.VISIBLE);
            trueFalseLayout.setVisibility(View.GONE);

            cOption1.setText(questions.getOptions()[0]);
            cOption2.setText(questions.getOptions()[1]);
            cOption3.setText(questions.getOptions()[2]);
            cOption4.setText(questions.getOptions()[3]);

        }
        else
        {
            radioGroup.setVisibility(View.GONE);
            checkBoxLayout.setVisibility(View.GONE);
            trueFalseLayout.setVisibility(View.VISIBLE);

            btnTrue.setText(questions.getOptions()[0]);
            btnFalse.setText(questions.getOptions()[1]);

        }
    }

    @SuppressLint("ResourceAsColor")
    private void clearInput()
    {
        radioGroup.clearCheck();
        cOption1.setChecked(false);
        cOption2.setChecked(false);
        cOption3.setChecked(false);
        cOption4.setChecked(false);
        btnFalse.setBackgroundResource(R.drawable.btn_background2);
        btnTrue.setBackgroundResource(R.drawable.btn_background2);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.false_btn)
        {
            btnFalse.setBackgroundResource(R.drawable.btn3);
            btnTrue.setBackgroundResource(R.drawable.btn_background2);
           p="False";
        }
        else
        {
            btnFalse.setBackgroundResource(R.drawable.btn_background2);
            btnTrue.setBackgroundResource(R.drawable.btn3);
            p="True";
        }
    }
}