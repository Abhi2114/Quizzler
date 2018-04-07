package com.abhinitsati.quizzler;


import java.util.ArrayList;

class TrueFalse {

    private String questionText;
    private boolean qAnswer;
    private String categories;

    TrueFalse(String qID, boolean trueOrFalse){

        questionText = qID;
        qAnswer = trueOrFalse;
        categories = "";
    }

    public TrueFalse(){}

    TrueFalse(String questionText, boolean qAnswer, String categories) {
        this.questionText = questionText;
        this.qAnswer = qAnswer;
        this.categories = categories;
    }

    String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setqAnswer(boolean qAnswer) {
        this.qAnswer = qAnswer;
    }

    boolean isqAnswer() {
        return qAnswer;
    }

    String getCategories(){

        return categories;
    }

    @Override
    public String toString() {

        return questionText + " " + qAnswer + " " + categories;

    }
}
