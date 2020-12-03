package com.example.quizapplication;

public class TopicWise
{
    private String topicName;
    private int correct,wrong;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public TopicWise(String topicName, int correct, int wrong) {
        this.topicName = topicName;
        this.correct = correct;
        this.wrong = wrong;
    }
}
