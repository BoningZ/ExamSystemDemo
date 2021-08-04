package com.company.bean;

import com.company.dao.QuestionDAO;
import com.company.dao.SheetDAO;
import com.company.utils.Consts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestPaper implements Serializable {
    private int id;
    private String title;
    private int status;
    private int timeLimit;
    private int[] questions=new int[50];
    private int numOfQuestions,sizeOfQuestions;
    private int[] answerSheets=new int[50];
    private int numOfAnswerSheets,sizeOfAnswerSheets;

    public boolean isDoneByStudent(String id){
        SheetDAO sheetDAO=new SheetDAO();
        for(int i=0;i<numOfAnswerSheets;i++) {
            AnswerSheet sheet=sheetDAO.findById(answerSheets[i]);
            if (sheet.getStudentId().equals(id)&&sheet.allCorrected()&&sheet.getNumOfAnswers()!=0) return true;
        }
        return false;
    }

    public double getFullMarks(){
        double result=0.0;
        QuestionDAO questionDAO=new QuestionDAO();
        for(int i=0;i<numOfQuestions;i++)
            result+=questionDAO.findById(questions[i]).getScore();
        return result;
    }
    
    public double getDifficultyOfCertainType(int type){
        double result=0.0;
        double fullMarks=getScoreOfCertainType(type);
        QuestionDAO questionDAO=new QuestionDAO();
        for(int i=0;i<numOfQuestions;i++){
            Question question=questionDAO.findById(questions[i]);
            if(question.getQuestionType()==type||type==Consts.TYPE_ALL)
                result+=question.getDifficulty()*question.getScore()/fullMarks;
        }
        return result;
    }

    public double getScoreOfCertainType(int type){
        if(type==Consts.TYPE_ALL)return getFullMarks();
        double result=0.0;
        QuestionDAO questionDAO=new QuestionDAO();
        for(int i=0;i<numOfQuestions;i++){
            Question question=questionDAO.findById(questions[i]);
            if(question.getQuestionType()==type)result+=question.getScore();
        }
        return result;
    }

    public int getNumOfCertainType(int type){
        int result=0;
        QuestionDAO questionDAO=new QuestionDAO();
        for(int i=0;i<numOfQuestions;i++){
            Question question=questionDAO.findById(questions[i]);
            if(question.getQuestionType()==type)result++;
        }
        return result;
    }
    
    public double getAverageScore(){
        SheetDAO sheetDAO=new SheetDAO();
        double sum=0.0;
        for(int i=0;i<numOfAnswerSheets;i++)
            sum+=sheetDAO.findById(answerSheets[i]).getScore();
        return sum/numOfAnswerSheets;
    }
    //Î¬»¤ questions
    public void addQuestion(int v){
        if(numOfQuestions==sizeOfQuestions)expandQuestions();
        for(int i=0;i<numOfQuestions;i++)
            if(questions[i]==v)return;
        questions[numOfQuestions++]=v;
    }
    public void removeQuestion(int v){
        int flag=0;
        for(int i=0;i<numOfQuestions-1;i++){
            if(questions[i]==v)flag=1;
            questions[i]=questions[i+flag];
        }
        if(flag==1||questions[numOfQuestions-1]==v)numOfQuestions--;
    }
    public List<Question> getQuestions(){
        List<Question> list=new ArrayList<>();
        QuestionDAO questionDAO=new QuestionDAO();
        for(int i=0;i<numOfQuestions;i++)
            list.add(questionDAO.findById(questions[i]));
        return list;
    }
    public String questionsToString(){
        if(numOfQuestions==0)return "";
        String st=""+questions[0];
        for(int i=1;i<numOfQuestions;i++)
            st+=","+questions[i];
        return st;
    }

    public void stringToQuestions(String st){
        questions=new int[50];
        numOfQuestions=0;
        sizeOfQuestions=50;
        Scanner sc=new Scanner(st);
        sc.useDelimiter(",");
        while (sc.hasNext()){
            if(numOfQuestions==sizeOfQuestions)expandQuestions();
            questions[numOfQuestions++]=Integer.parseInt(sc.next());
        }
    }
    private void expandQuestions(){
        int[] temp=new int[sizeOfQuestions<<1];
        for(int i=0;i<numOfQuestions;i++)
            temp[i]=questions[i];
        questions=temp;
        sizeOfQuestions<<=1;
    }

    public Question getQuestionByRank(int rank){
        QuestionDAO questionDAO=new QuestionDAO();
        return questionDAO.findById(questions[rank]);
    }

    public int getQuestionIdByRank(int rank){return questions[rank];}
    //Î¬»¤answer sheets
    public void addAnswerSheet(int v){
        if(numOfAnswerSheets==sizeOfAnswerSheets)expandSheets();
        answerSheets[numOfAnswerSheets++]=v;
    }
    public String answerSheetsToString(){
        if(numOfAnswerSheets==0)return "";
        String st=""+answerSheets[0];
        for(int i=1;i<numOfAnswerSheets;i++)
            st+=","+answerSheets[i];
        return st;
    }

    public void stringToAnswerSheets(String st){
        answerSheets=new int[50];
        numOfAnswerSheets=0;
        sizeOfAnswerSheets=50;
        Scanner sc=new Scanner(st);
        sc.useDelimiter(",");
        while (sc.hasNext()){
            if(numOfAnswerSheets==sizeOfAnswerSheets)expandSheets();
            answerSheets[numOfAnswerSheets++]=Integer.parseInt(sc.next());
        }
    }
    private void expandSheets(){
        int[] temp=new int[sizeOfAnswerSheets<<1];
        for(int i=0;i<numOfAnswerSheets;i++)
            temp[i]=answerSheets[i];
        answerSheets=temp;
        sizeOfAnswerSheets<<=1;
    }

    public AnswerSheet getAnswerSheetByRank(int rank){
        SheetDAO sheetDAO=new SheetDAO();
        return sheetDAO.findById(answerSheets[rank]);
    }

    public List<AnswerSheet> getToCorrect(){
        SheetDAO sheetDAO=new SheetDAO();
        List<AnswerSheet> list=new ArrayList<AnswerSheet>();
        for(int i=0;i<numOfAnswerSheets;i++){
            AnswerSheet sheet=sheetDAO.findById(answerSheets[i]);
            if(sheet.getStatus()==Consts.STATE_WAITING_CORR){
                if(sheet.allCorrected()){
                    sheet.setStatus(Consts.STATE_CORRECTED);
                    sheetDAO.update(sheet,sheet.getId());
                    continue;
                }
                list.add(sheet);
            }
        }
        return list;
    }

    public List<AnswerSheet> getCorrected(){
        SheetDAO sheetDAO=new SheetDAO();
        List<AnswerSheet> list=new ArrayList<AnswerSheet>();
        for(int i=0;i<numOfAnswerSheets;i++){
            AnswerSheet sheet=sheetDAO.findById(answerSheets[i]);
            if(sheet.getStatus()==Consts.STATE_CORRECTED)list.add(sheet);
        }
        return list;
    }

    public TestPaper(){}

    public TestPaper(int id, String title, int status, int timeLimit) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.timeLimit = timeLimit;
        sizeOfQuestions=4;
        sizeOfAnswerSheets=4;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    public void setNumOfQuestions(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    public int getSizeOfQuestions() {
        return sizeOfQuestions;
    }

    public void setSizeOfQuestions(int sizeOfQuestions) {
        this.sizeOfQuestions = sizeOfQuestions;
    }

    public int getNumOfAnswerSheets() {
        return numOfAnswerSheets;
    }

    public void setNumOfAnswerSheets(int numOfAnswerSheets) {
        this.numOfAnswerSheets = numOfAnswerSheets;
    }

    public int getSizeOfAnswerSheets() {
        return sizeOfAnswerSheets;
    }

    public void setSizeOfAnswerSheets(int sizeOfAnswerSheets) {
        this.sizeOfAnswerSheets = sizeOfAnswerSheets;
    }
}
