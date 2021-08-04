package com.company.bean;

import com.company.dao.AnswerDAO;
import com.company.dao.PaperDAO;
import com.company.dao.SheetDAO;
import com.company.dao.UserDAO;
import com.company.utils.Consts;

import java.io.Serializable;
import java.util.*;

public class AnswerSheet implements Serializable,Comparable {
    private String studentId;
    private int testPaperId;
    private int id;
    private int[] answers=new int[4];
    private int numOfAnswers,sizeOfAnswers;
    private int status;

    public String getTestPaperTitle(){
        PaperDAO paperDAO=new PaperDAO();
        return paperDAO.findById(testPaperId).getTitle();
    }


    public User getOriginStudent(){
        UserDAO userDAO=new UserDAO();
        return userDAO.findById(studentId);
    }

    public TestPaper getOriginTestPaper(){
        PaperDAO paperDAO=new PaperDAO();
        return paperDAO.findById(testPaperId);
    }

    public double getScore(){
        double result=0.0;
        AnswerDAO answerDAO=new AnswerDAO();
        for(int i=0;i<numOfAnswers;i++)
            result+=answerDAO.findById(answers[i]).getScore();
        return result;
    }

    public int getRank(){
        List<Double> list=new ArrayList<>();
        PaperDAO paperDAO=new PaperDAO();
        TestPaper paper=paperDAO.findById(testPaperId);
        for(int i=0;i<paper.getNumOfAnswerSheets();i++){
            AnswerSheet sheet=paper.getAnswerSheetByRank(i);
            //if(!sheet.allCorrected())return -1;
            list.add(sheet.getScore());
        }
        double myScore=getScore();
        list.sort(new Comparator<Double>() {
            public int compare(Double o1, Double o2){if(o2>o1)return 1;else return -1;
            }});
        int i=1;
        for(double score:list){
            if(score==myScore)return i;
            i++;
        }
        return i;
    }

    public double getScoreOfCertainType(int type){
        double result=0.0;
        AnswerDAO answerDAO=new AnswerDAO();
        for(int i=0;i<numOfAnswers;i++) {
            Answer answer=answerDAO.findById(answers[i]);
            if(answer.getOriginQuestion().getQuestionType()==type)
                result+=answer.getScore();
        }
        return result;
    }

    public boolean allCorrected(){
        AnswerDAO answerDAO=new AnswerDAO();
        for(int i=0;i<numOfAnswers;i++)
            if(!answerDAO.findById(answers[i]).isCorrected())return false;
        return true;
    }



    public void autoCorrect(){
        AnswerDAO answerDAO=new AnswerDAO();
        for(int i=0;i<numOfAnswers;i++)
            answerDAO.findById(answers[i]).autoCorrect();
    }

    public void addAnswer(int v){
        if(numOfAnswers==sizeOfAnswers)expand();
        answers[numOfAnswers++]=v;
    }
    public String answersToString(){
        if(numOfAnswers==0)return "";
        String st=""+answers[0];
        for(int i=1;i<numOfAnswers;i++)
            st+=","+answers[i];
        return st;
    }

    public void stringToAnswers(String st){
        answers=new int[50];
        numOfAnswers=0;
        sizeOfAnswers=50;
        Scanner sc=new Scanner(st);
        sc.useDelimiter(",");
        while (sc.hasNext()){
            if(numOfAnswers==sizeOfAnswers)expand();
            answers[numOfAnswers++]=Integer.parseInt(sc.next());
        }
    }
    private void expand(){
        int[] temp=new int[sizeOfAnswers<<1];
        for(int i=0;i<numOfAnswers;i++)
            temp[i]=answers[i];
        answers=temp;
        sizeOfAnswers<<=1;
    }

    public Answer getAnswerByRank(int rank){
        AnswerDAO answerDAO=new AnswerDAO();
        return answerDAO.findById(answers[rank]);
    }

    public int getAnswerIdByRank(int rank){
        return answers[rank];
    }

    public AnswerSheet(){}
    public AnswerSheet(String studentId, int testPaperId, int id) {
        this.status= Consts.STATE_WAITING_ANS;
        this.studentId = studentId;
        this.testPaperId = testPaperId;
        this.id = id;
        sizeOfAnswers=4;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getTestPaperId() {
        return testPaperId;
    }

    public void setTestPaperId(int testPaperId) {
        this.testPaperId = testPaperId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumOfAnswers() {
        return numOfAnswers;
    }

    public void setNumOfAnswers(int numOfAnswers) {
        this.numOfAnswers = numOfAnswers;
    }

    public int getSizeOfAnswers() {
        return sizeOfAnswers;
    }

    public void setSizeOfAnswers(int sizeOfAnswers) {
        this.sizeOfAnswers = sizeOfAnswers;
    }

    @Override
    public int compareTo(Object o) {
        AnswerSheet a=(AnswerSheet)o;
        if(getScore()>a.getScore())return 1;
         else return 0;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
