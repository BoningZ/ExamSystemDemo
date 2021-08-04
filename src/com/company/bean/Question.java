package com.company.bean;

import com.company.utils.Consts;

import java.io.Serializable;

public class Question implements Serializable,Comparable {
    private int id;
    private String description;
    private String title;
    private boolean isChoice;//1:单选 2:多选 3:填空 4:简答
    private double difficulty;
    private double score;

    private boolean isMultiple;
    private int objAnswer;
    private int numOfOptions;

    private String subAnswer;
    private boolean isShortAnswer;

    public int getQuestionType(){
        if(isMultiple)return 2;
        if(isChoice)return 1;
        if(isShortAnswer)return 4;
        return 3;
    }

    public Question(){}

    //objective question
    public Question(int id, String description, String title, double difficulty, double score, boolean isMultiple, int objAnswer, int numOfOptions) {
        this.id = id;
        this.description = description;
        this.title = title;
        this.isChoice = true;
        this.difficulty = difficulty;
        this.score = score;
        this.isMultiple = isMultiple;
        this.objAnswer = objAnswer;
        this.numOfOptions = numOfOptions;
    }
    //subjective question
    public Question(int id, String description, String title, double difficulty, double score, String subAnswer, boolean isShortAnswer) {
        this.id = id;
        this.description = description;
        this.title = title;
        this.isChoice = false;
        this.difficulty = difficulty;
        this.score = score;
        this.subAnswer = subAnswer;
        this.isShortAnswer = isShortAnswer;
    }
    public Question(int id,int type){

    }
    public boolean isMultiple() {
        return isMultiple;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    public int getObjAnswer() {
        return objAnswer;
    }

    public void setObjAnswer(int objAnswer) {
        this.objAnswer = objAnswer;
    }

    public int getNumOfOptions() {
        return numOfOptions;
    }

    public void setNumOfOptions(int numOfOptions) {
        this.numOfOptions = numOfOptions;
    }

    public String getSubAnswer() {
        return subAnswer;
    }

    public void setSubAnswer(String subAnswer) {
        this.subAnswer = subAnswer;
    }

    public boolean isShortAnswer() {
        return isShortAnswer;
    }

    public void setShortAnswer(boolean shortAnswer) {
        isShortAnswer = shortAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChoice() {
        return isChoice;
    }

    public void setChoice(boolean choice) {
        isChoice = choice;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int compareTo(Object o) {
        Question q=(Question)o;
        if(difficulty>q.getDifficulty())return 1;
            else return 0;
    }
}
