package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Questions implements Parcelable {
    private String questionId;
    private String surveyId;
    private String question;
    private String questionType;
    private List<Options> options;


    public Questions(){}


    protected Questions(Parcel in) {
        questionId = in.readString();
        surveyId = in.readString();
        question = in.readString();
        questionType = in.readString();
        options = in.createTypedArrayList(Options.CREATOR);
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

    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(questionId);
        parcel.writeString(surveyId);
        parcel.writeString(question);
        parcel.writeString(questionType);
        parcel.writeTypedList(options);
    }
}
