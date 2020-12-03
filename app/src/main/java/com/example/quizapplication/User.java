package com.example.quizapplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

public class User
{


    public User() {
        }

    public User(String name, String institute, String registrationDate, LinkedHashMap<String, Integer> dailyQuestionTally, ArrayList<TopicWise> topicWiseTally, LinkedHashMap<String, Integer> quizzScores) {
        this.name = name;
        this.institute = institute;
        this.registrationDate = registrationDate;
        this.dailyQuestionTally = dailyQuestionTally;
        this.topicWiseTally = topicWiseTally;
        this.quizzScores = quizzScores;
    }

    private String name, institute, registrationDate;
        private LinkedHashMap<String, Integer> dailyQuestionTally;
        private ArrayList<TopicWise> topicWiseTally;
        private LinkedHashMap<String, Integer> quizzScores;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LinkedHashMap<String, Integer> getDailyQuestionTally() {
        return dailyQuestionTally;
    }

    public void setDailyQuestionTally(LinkedHashMap<String, Integer> dailyQuestionTally) {
        this.dailyQuestionTally = dailyQuestionTally;
    }

    public ArrayList<TopicWise> getTopicWiseTally() {
        return topicWiseTally;
    }

    public void setTopicWiseTally(ArrayList<TopicWise> topicWiseTally) {
        this.topicWiseTally = topicWiseTally;
    }

    public LinkedHashMap<String, Integer> getQuizzScores() {
        return quizzScores;
    }

    public void setQuizzScores(LinkedHashMap<String, Integer> quizzScores) {
        this.quizzScores = quizzScores;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static User loadUser(Context context) {
        User userx=new User();
        String userJsonStr = loadJSONFromAsset("testuser.json",context);

        try {
            JSONObject user = new JSONObject(userJsonStr);
            userx.setName(user.getString("name"));
            userx.setInstitute(user.getString("institute"));
            userx.setRegistrationDate(user.getString("registrationDate"));

            //daily que tally
            JSONArray jsonArray = user.getJSONArray("queDailyTally");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                userx.getDailyQuestionTally().put(jsonObject.getString("date"), jsonObject.getInt("noOfQue"));
            }
            //topic questions
            JSONArray queTopicwiseTally = user.getJSONArray("queTopicwiseTally");
            for (int i = 0; i < queTopicwiseTally.length(); i++) {
                JSONObject jsonObject = (JSONObject) queTopicwiseTally.get(i);
                userx.getTopicWiseTally().add(new TopicWise(jsonObject.getString("topic"), jsonObject.getInt("correct"), jsonObject.getInt("wrong")));
            }
            //Quizz scores
            JSONArray quizzScoresJsonArray = user.getJSONArray("quizzTally");
            int t = 10;
            for (int i = 0; i < quizzScoresJsonArray.length(); i++) {
                if (quizzScoresJsonArray.getInt(i) != 0) {
                    userx.getQuizzScores().put(t+"%", quizzScoresJsonArray.getInt(i));
                    t = t + 10;
                }
            }
        } catch (JSONException e) {
            Toast.makeText(context, " "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return userx;
    }

    //load data from json file
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String loadJSONFromAsset(String file, Context context) {
        String json = "";
        try {
            InputStream is = context.getAssets().open(file);
            int size = is.available();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static User getUser(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("QuizAppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userJsonStr = sharedPreferences.getString("userInfo", null);
        if (userJsonStr != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<User>() {}.getType();
            User user = gson.fromJson(userJsonStr, type);
            return user;
        } else {
            Log.i("sptest", "Problem in loading userJsonStr from sp");
            return null;
        }
    }

    public static void updateUser(Context context,User user)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("QuizAppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userJsonStr = new Gson().toJson(user);
        editor.putString("userInfo",userJsonStr).apply();

    }

    public static String getCurrentDate()
    {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        return strDate;
    }

}
