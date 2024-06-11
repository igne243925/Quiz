package com.jtdev.knowsalot;

import java.util.List;


// parang extension lang to hehe
public class Question {
    private String question;
    private List<String> choices;
    private int rightAnswerIndex;

    public Question() {
        // Default constructor required for Firebase
    }

    public Question(String question, List<String> choices, int rightAnswerIndex) {
        this.question = question;
        this.choices = choices;
        this.rightAnswerIndex = rightAnswerIndex;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public int getRightAnswerIndex() {
        return rightAnswerIndex;
    }

    public void setRightAnswerIndex(int rightAnswerIndex) {
        this.rightAnswerIndex = rightAnswerIndex;
    }
}