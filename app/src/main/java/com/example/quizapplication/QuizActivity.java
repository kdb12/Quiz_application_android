package com.example.quizapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapplication.Fragments.QuestionList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class QuizActivity extends AppCompatActivity implements QuestionList.QuestionListener ,View.OnClickListener {


    TextView textView,textView2,timer;
    ArrayList<Questions> totalQuestions;
    Button btnEnd,btnTrue,btnFalse;
    EditText etQuestion;
    RadioGroup radioGroup;
    ImageButton btnPrev,btnNext;
    RadioButton rOption1,rOption2,rOption3,rOption4;
    CheckBox cOption1,cOption2,cOption3,cOption4;
    LinearLayout checkBoxLayout,trueFalseLayout;
    int currentQuestion=0;
    HashMap<Integer,boolean[]> map;
    private CountDownTimer countDownTimer;
    private static final String FORMAT = "%02d:%02d:%02d";
    private long timeTotal;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        btnEnd=findViewById(R.id.btnEnd);
        map=new HashMap<>();
        btnPrev=findViewById(R.id.btnPrev);
        btnNext=findViewById(R.id.btnNext);
        timer=findViewById(R.id.timer);
        textView=findViewById(R.id.quiz_name_2);
        textView2=findViewById(R.id.title_of_quiz);
        etQuestion=findViewById(R.id.question_description);
        radioGroup=findViewById(R.id.rgroup);
        checkBoxLayout=findViewById(R.id.checkbox_layout);
        trueFalseLayout=findViewById(R.id.true_false_layout);

        rOption1 =findViewById(R.id.opt1);
        rOption2 =findViewById(R.id.opt2);
        rOption3 =findViewById(R.id.opt3);
        rOption4 =findViewById(R.id.opt4);

        cOption1 =findViewById(R.id.copt1);
        cOption2 =findViewById(R.id.copt2);
        cOption3 =findViewById(R.id.copt3);
        cOption4 =findViewById(R.id.copt4);

        btnFalse=findViewById(R.id.false_button);
        btnTrue=findViewById(R.id.true_button);

        totalQuestions=new ArrayList<>();

        textView.setText(String.valueOf(getIntent().getIntExtra("Quiz_number",1)));
        textView2.setText(getIntent().getStringExtra("Quiz_title"));

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                endQuiz();
            }
        });

        loadAllQuestions();
        getSharedPreferences("totalQuestions",MODE_PRIVATE).edit().putInt("size",totalQuestions.size()).apply();
        displayQuestion(0);
        getSupportFragmentManager().beginTransaction().replace(R.id.question_recycle_fragment,new QuestionList(),"dd").commit();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestion++;
                displayQuestion(currentQuestion);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestion--;
                displayQuestion(currentQuestion);
            }
        });

        btnTrue.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                btnTrue.setBackgroundResource(R.drawable.btn3);
                btnFalse.setBackgroundResource(R.drawable.btn_background2);
                boolean[] answers = new boolean[2];
                answers[0]=true;
                answers[1]=false;
                if(map.containsKey(currentQuestion))
                {
                    map.replace(currentQuestion, answers);
                }
                else {
                    map.put(currentQuestion, answers);
                }
            }
        });

        btnFalse.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                btnFalse.setBackgroundResource(R.drawable.btn3);
                btnTrue.setBackgroundResource(R.drawable.btn_background2);

                boolean[] answers = new boolean[2];

                answers[0]=false;
                answers[1]=true;

                if(map.containsKey(currentQuestion))
                {
                    map.replace(currentQuestion, answers);
                }
                else {
                    map.put(currentQuestion, answers);
                }
            }
        });

        cOption1.setOnClickListener(this);
        cOption2.setOnClickListener(this);
        cOption3.setOnClickListener(this);
        cOption4.setOnClickListener(this);

        RadioListener radioListener=new RadioListener();

        rOption1.setOnClickListener(radioListener);
        rOption2.setOnClickListener(radioListener);
        rOption3.setOnClickListener(radioListener);
        rOption4.setOnClickListener(radioListener);

        countDownTimer=new CountDownTimer(timeTotal,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeTotal = millisUntilFinished;
                updateTimer();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFinish() {
                endQuiz();
            }
        }.start();
    }

    private void updateTimer() {
        int min=(int) timeTotal/60000;
        int sec=(int) timeTotal%60000/1000;

        if(min<1)
        {
            timer.setTextColor(Color.argb(255,255,0,0));
        }

        String sTime = null;
        if(min <10) sTime = "0";
        sTime += ""+min;
        sTime += ":";

        if(sec<10) sTime += "0";
        sTime += sec;

        timer.setText(sTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadAllQuestions() {
        String path=textView2.getText().toString()+".json";
        //Toast.makeText(this, "path="+path, Toast.LENGTH_SHORT).show();
        String jsonQuiz=loadJSONString(path);

        try {
            JSONObject Quiz=new JSONObject(jsonQuiz);
            int total=Quiz.getInt("totalQue");
            String time=Quiz.getString("time");
            timeTotal=Long.parseLong(time);
            timeTotal=timeTotal*60*1000;
            JSONArray QuestionArray=Quiz.getJSONArray("questions");

            //Toast.makeText(this, "length = "+QuestionArray.length(), Toast.LENGTH_SHORT).show();

            for(int i=0;i<QuestionArray.length();i++)
            {
                Questions questionTemp=new Questions();
                JSONObject Question=QuestionArray.getJSONObject(i);
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
                totalQuestions.add(questionTemp);
            }

        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle("SORRY");
        builder.setMessage("YOU CAN NOT DO THIS");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String loadJSONString(String file) {

        String json = "";
        try {

            InputStream is = getAssets().open("quizes/"+file);
            int size = is.available();
            //Toast.makeText(this, "size="+""+size, Toast.LENGTH_SHORT).show();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void displayQuestion(int position) {

        currentQuestion=position;

        if(position==0)
        {
            btnPrev.setVisibility(View.INVISIBLE);
        }
        else if(position==totalQuestions.size()-1)
        {
            btnNext.setVisibility(View.INVISIBLE);
        }
        else
        {
            btnPrev.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
        }

        Questions questions=totalQuestions.get(position);
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

            if(map.containsKey(position))
            {

                boolean[] value=map.get(position);

                for(int i=0;i<5;i++)
                {
                    if(value[i])
                    {
                        switch(i)
                        {
                            case 1:rOption1.setChecked(true);break;
                            case 2:rOption2.setChecked(true);break;
                            case 3:rOption3.setChecked(true);break;
                            case 4:rOption4.setChecked(true);break;
                        }
                    }
                }
            }
            else
            {
                clearInput();
            }

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



            if(map.containsKey(position))
            {
                boolean[] ans=map.get(position);
                for(int i=1;i<5;i++)
                {
                    if(ans[i])
                    {
                        switch(i)
                        {
                            case 1:cOption1.setChecked(true);break;
                            case 2:cOption2.setChecked(true);break;
                            case 3:cOption3.setChecked(true);break;
                            case 4:cOption4.setChecked(true);break;
                        }
                    }
                }
            }
            else
            {
                clearInput();
            }
        }
        else
        {
            radioGroup.setVisibility(View.GONE);
            checkBoxLayout.setVisibility(View.GONE);
            trueFalseLayout.setVisibility(View.VISIBLE);

            btnTrue.setText(questions.getOptions()[0]);
            btnFalse.setText(questions.getOptions()[1]);

            if(map.containsKey(position))
            {
                boolean[] ans=map.get(position);

                if(ans[0]==true)
                {
                    btnTrue.setBackgroundResource(R.drawable.btn3);
                    btnFalse.setBackgroundResource(R.drawable.btn_background2);
                }
                else
                {
                    btnFalse.setBackgroundResource(R.drawable.btn3);
                    btnTrue.setBackgroundResource(R.drawable.btn_background2);
                }
            }
            else
            {
                clearInput();
            }
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        boolean[] answers=new boolean[5];
        for(int i=0;i<5;i++)
        {
            answers[i]=false;
        }
        if(cOption1.isChecked())
        {
            answers[1]=true;
        }
        if(cOption2.isChecked())
        {
            answers[2]=true;
        }
        if(cOption3.isChecked())
        {
            answers[3]=true;
        }
        if(cOption4.isChecked())
        {
            answers[4]=true;
        }
        if(map.containsKey(currentQuestion))
        {
            map.replace(currentQuestion, answers);
        }
        else {
            map.put(currentQuestion, answers);
        }
    }





    public class RadioListener implements View.OnClickListener
    {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            boolean[] answers=new boolean[5];
            for(int i=0;i<5;i++)
            {
                answers[i]=false;
            }
            switch (v.getId())
            {
                case R.id.opt1:
                    answers[1]=true;
                    break;
                case R.id.opt2:
                    answers[2]=true;
                    break;

                case R.id.opt3:
                    answers[3]=true;
                    break;

                case R.id.opt4:
                    answers[4]=true;
                    break;
            }
            if(map.containsKey(currentQuestion))
            {
                map.replace(currentQuestion, answers);
            }
            else {
                map.put(currentQuestion, answers);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void endQuiz()
    {
        //User u=new User();
        AlertDialog.Builder builder=new AlertDialog.Builder(QuizActivity.this);
        builder.setTitle("RESULT");
        int marks_got = 0,total_marks=totalQuestions.size();
        int id=0;
        for(Questions q:totalQuestions)
        {
            boolean[] ans = new boolean[5];
            for(int i=0;i<5;i++)
            {
                ans[i]=false;
            }
            for(int i=0;i<q.getCorrect().length;i++)
            {
                for(int j=0;j<q.getOptions().length;j++)
                {
                    if(q.getCorrect()[i].equals(q.getOptions()[j]))
                    {
                        ans[j+1]=true;
                    }
                }
            }
            if(q.getType()!=3) {
                boolean flag = false;
                if (map.containsKey(id)) {
                    boolean[] myans = map.get(id);
                    for (int i = 0; i < 5; i++) {
                        if (myans[i] == ans[i]) {
                            flag = true;
                        } else {
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag) {
                    correctSolved(q);
                    marks_got++;
                }
                else
                {
                    wrongSolved(q);
                }
            }
            else
            {
                if (map.containsKey(id))
                {
                    boolean[] myans = map.get(id);
                    if(myans[0]==ans[1] && myans[1]==ans[2])
                    {
                        marks_got++;
                        correctSolved(q);
                    }
                    else
                    {
                        wrongSolved(q);
                    }
                }

            }
            id++;
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(QuizActivity.this,MainActivity.class));
                finish();
            }
        });
        builder.setMessage(marks_got+"/"+total_marks);
        builder.create().show();

        int per=(marks_got*100)/total_marks;

        //per=50;

        Log.d("PERCENTAGE",per+"");

        for(int i=10;i<=100;i+=10)
        {
            if(per<i)
            {
                per=i-10;
                break;
            }
        }

        Log.d("PERCENTAGE",per+"");

        User user=User.getUser(QuizActivity.this);

        if(user.getQuizzScores().containsKey(String.valueOf(per)))
        {

            Log.i("quiz score",user.getQuizzScores().toString());
           int total=user.getQuizzScores().get(String.valueOf(per));
            user.getQuizzScores().replace(String.valueOf(per),total+1);

        }
        else {
            user.getQuizzScores().put(String.valueOf(per),1);
        }

        User.updateUser(QuizActivity.this,user);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void wrongSolved(Questions questions) {
        User user= User.getUser(QuizActivity.this);
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
        User.updateUser(QuizActivity.this,user);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void correctSolved(Questions questions) {
        User user= User.getUser(QuizActivity.this);
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

        User.updateUser(QuizActivity.this,user);

    }
}