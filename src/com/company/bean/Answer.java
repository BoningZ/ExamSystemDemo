package com.company.bean;

import com.company.dao.AnswerDAO;
import com.company.dao.QuestionDAO;
import com.company.utils.Consts;

import java.io.Serializable;

public class Answer implements Serializable {
    private int questionId;
    private int id;
    private double score;
    private int objAnswer;
    private String subAnswer;
    private boolean isCorrected;

    public boolean isTrue(){
        QuestionDAO questionDAO=new QuestionDAO();
        Question question=questionDAO.findById(questionId);
        return Math.abs(question.getScore() - score) < 0.0001;
    }

    boolean autoCorrect(){
        QuestionDAO questionDAO=new QuestionDAO();
        Question question=questionDAO.findById(questionId);
        if(question.isChoice()){
            score=(objAnswer==question.getObjAnswer())?question.getScore():0.0;
            if(question.getQuestionType()== Consts.TYPE_MULTI_CHOICE&&score==0.0&&objAnswer!=0){
                int a=question.getObjAnswer(),b=objAnswer,flag=0;
                while(b>0){
                    if(b%2==1&&a%2==0){flag=1;break;}
                    b>>=1; a>>=1;
                }
                if(flag==0)score=question.getScore()*0.6;
            }
            isCorrected=true;
            AnswerDAO answerDAO=new AnswerDAO();
            answerDAO.update(this,id);
        }
        return question.isChoice();
    }

    public void manuallyCorrect(double score){
        setScore(score);
        isCorrected=true;
    }

    public Question getOriginQuestion(){
        QuestionDAO questionDAO=new QuestionDAO();
        return questionDAO.findById(questionId);
    }

    public Answer(){}
    //objective answer
    public Answer(int questionId, int id, double score, int objAnswer) {
        this.questionId = questionId;
        this.id = id;
        this.score = score;
        this.objAnswer = objAnswer;
    }

    //subjective answer
    public Answer(int questionId, int id, double score, String subAnswer) {
        this.questionId = questionId;
        this.id = id;
        this.score = score;
        this.subAnswer = subAnswer;
    }

    public Answer(int id,int questionId){
        this.id=id;
        this.questionId=questionId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getObjAnswer() {
        return objAnswer;
    }

    public void setObjAnswer(int objAnswer) {
        this.objAnswer = objAnswer;
    }

    public String getSubAnswer() {
        return subAnswer;
    }

    public void setSubAnswer(String subAnswer) {
        this.subAnswer = subAnswer;
    }

    public boolean isCorrected() {
        return isCorrected;
    }

    public void setCorrected(boolean corrected) {
        isCorrected = corrected;
    }
}
