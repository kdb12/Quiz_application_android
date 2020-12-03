package com.example.quizapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Questions implements Parcelable {



   int type;
   String Question;
   String[] Options;
   String[] Correct;
   String[] topic;
   boolean lastSolved;

    protected Questions(Parcel in) {
        type = in.readInt();
        Question = in.readString();
        Options = in.createStringArray();
        Correct = in.createStringArray();
        topic = in.createStringArray();
        lastSolved = in.readByte() != 0;
    }

    public static final Creator<Questions> CREATOR = new Creator<Questions>() {
        @Override
        public Questions createFromParcel(Parcel in) {
            return new Questions(in);
        }

        @Override
        public Questions[] newArray(int size) {
            return new Questions[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String[] getOptions() {
        return Options;
    }

    public void setOptions(String[] options) {
        Options = options;
    }

    public String[] getCorrect() {
        return Correct;
    }

    public void setCorrect(String[] correct) {
        Correct = correct;
    }

    public String[] getTopic() {
        return topic;
    }

    public void setTopic(String[] topic) {
        this.topic = topic;
    }

    public boolean getLastSolved() {
        return lastSolved;
    }

    public void setLastSolved(boolean lastSolved) {
        this.lastSolved = lastSolved;
    }

    public Questions(int type, String question, String[] options, String[] correct, String[] topic, boolean lastSolved) {
        this.type = type;
        Question = question;
        Options = options;
        Correct = correct;
        this.topic = topic;
        this.lastSolved = lastSolved;
    }

    public Questions() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(Question);
        dest.writeStringArray(Options);
        dest.writeStringArray(Correct);
        dest.writeStringArray(topic);
        dest.writeByte((byte) (lastSolved ? 1 : 0));
    }
}
