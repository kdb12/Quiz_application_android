package com.example.quizapplication.Fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.quizapplication.R;
import com.example.quizapplication.TopicWise;
import com.example.quizapplication.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;


public class ProfileFragment extends Fragment {



    public ProfileFragment() {
        // Required empty public constructor
    }

//    String username, institute;
//    String registrationDate;
//    int rating;
//    LinkedHashMap<String, Integer> dailyQueTally;
//    ArrayList<TopicWise> topicWiseList;
    ArrayList<String> topics;
//    LinkedHashMap<String, Integer> quizzScores;

    BarChart barChart, stackedBarChart;
    PieChart quizzScoresPieChart;

    TextInputEditText nameTV, instituteTV, registrationDateTV;
     TextView ratingTV;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        int[] colorClassArray = {Color.GREEN, Color.RED};

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        barChart = view.findViewById(R.id.dailyQueTallyBarChart);
        stackedBarChart = view.findViewById(R.id.topicWiseTally);
        quizzScoresPieChart = view.findViewById(R.id.quizzScorePieChart);
        nameTV = view.findViewById(R.id.nameTV);
        instituteTV = view.findViewById(R.id.instituteTV);
        registrationDateTV = view.findViewById(R.id.registrationDateTV);
        ratingTV = view.findViewById(R.id.ratingTV);

        ratingTV.setVisibility(View.GONE);

//        dailyQueTally = new LinkedHashMap<>();
//        topicWiseList = new ArrayList<>();
        topics = new ArrayList<>();
//
        ArrayList<BarEntry> barDataSetData = new ArrayList<>();
        Iterator<String> iterator = User.getUser(getContext()).getDailyQuestionTally().keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();

                int day = Integer.parseInt(key.substring(8));

                barDataSetData.add(new BarEntry(day, User.getUser(getContext()).getDailyQuestionTally().get(key)));

        }
        ArrayList<BarEntry> stackedBarChartDataSetdata = new ArrayList<>();
        for (int i = 0; i <  User.getUser(getContext()).getTopicWiseTally().size(); i++) {
            stackedBarChartDataSetdata.add(new BarEntry(i, new float[] {User.getUser(getContext()).getTopicWiseTally().get(i).getCorrect(), User.getUser(getContext()).getTopicWiseTally().get(i).getWrong()}));
            topics.add(User.getUser(getContext()).getTopicWiseTally().get(i).getTopicName());
        }
        //--

        //quiz score
        ArrayList<PieEntry> pieEntryArrayList = new ArrayList<>();
        int t = 0;
        for (int i = 0; i <10; i++) {
            if(User.getUser(getContext()).getQuizzScores().containsKey(String.valueOf(t))) {
                pieEntryArrayList.add(new PieEntry(User.getUser(getContext()).getQuizzScores().get(String.valueOf(t)), t + "%"));
               // pieEntryArrayList.add(new PieEntry(User.getUser(getContext()).getQuizzScores().get(String.valueOf(t),t+"%")));
            }
            t = t + 10;
        }
        //--



        BarDataSet barDataSet = new BarDataSet(barDataSetData, "Daily Questions Tally");
        BarData barData = new BarData();
        barData.setBarWidth(0.2f);
        barData.addDataSet(barDataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setLabelCount(7);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        barChart.setDrawBorders(true);
        barChart.invalidate();

        //BarDataSet stackedBarChartDataset = new BarDataSet(stackedBarChartDataSetdata, "Topicwise Questions Tally");
        BarDataSet stackedBarChartDataset = new BarDataSet(stackedBarChartDataSetdata, "");
        stackedBarChartDataset.setColors(colorClassArray);
        stackedBarChartDataset.setStackLabels(new String[] {"Correct", "Wrong"});
        BarData stackedBarData = new BarData();
        stackedBarData.addDataSet(stackedBarChartDataset);
        stackedBarData.setBarWidth(0.2f);
        stackedBarChart.setData(stackedBarData);
        stackedBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(topics));
        stackedBarChart.getDescription().setEnabled(false);
        stackedBarChart.getXAxis().setDrawGridLines(false);
        stackedBarChart.getAxisRight().setEnabled(false);
        stackedBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        stackedBarChart.setDrawBorders(true);
        stackedBarChart.invalidate();

        PieDataSet pieDataSet = new PieDataSet(pieEntryArrayList, "Pie DataSet");
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        quizzScoresPieChart.setData(pieData);
        quizzScoresPieChart.getDescription().setEnabled(false);
        quizzScoresPieChart.setExtraOffsets(5, 10, 5, 5);
        quizzScoresPieChart.invalidate();





        nameTV.setText(User.getUser(getContext()).getName());

        instituteTV.setText(User.getUser(getContext()).getInstitute());

        registrationDateTV.setText(User.getUser(getContext()).getRegistrationDate());
        ratingTV.setText("5.0");

        return view;
    }








}