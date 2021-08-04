package com.company.service;

import com.company.bean.*;
import com.company.exception.NameOrPasswordException;
import com.company.exception.PasswordNotMatchException;
import com.company.exception.UserExistException;

import java.util.List;

public interface ExamService {
    User login(String id, String password, boolean isStudent) throws NameOrPasswordException;
    User register(String username, String id, String password, String repeatPassword, boolean isStudent, int grade) throws UserExistException, PasswordNotMatchException;
    List<TestPaper> getDoneExam(String userId);
    List<TestPaper> getTodoExam(String userId);
    Object[][] getDoneInfo(String userId);
    Object[][] getTableData(TestPaper paper);
    List<TestPaper> getReleased(String userId);
    List<TestPaper> getUnreleased(String userId);
    List<AnswerSheet> getToCorrect(TestPaper paper);
    List<AnswerSheet> getCorrected(TestPaper paper);

    Question createQuestion(int type);
    TestPaper createPaper(User u);
    Answer createAnswer(int questionId);

    List<Question> screenQuestion(boolean singleChoice,boolean multiChoice,boolean fillBlank,boolean shortAns);
    void deleteQuestion(int id);

    Question findQuestion(int id);
    TestPaper findPaper(int id);
    Answer findAnswer(int id);

    void updateQuestion(int id,Question q);
    void updatePaper(TestPaper paper);
    void updateSheet(AnswerSheet sheet);
    void updateAnswer(Answer answer);

    TestPaper generatePaper(int numOfSingle, int numOfMulti, int numOfFill, int numOfShort,double scoreOfSingle, double scoreOfMulti, double scoreOfFill, double scoreOfShort,double diff);
    int getNumOfType(TestPaper paper,int type);
    double getScoreOfType(TestPaper paper,int type);
    double getDiffOfType(TestPaper paper,int type);
    List<Question> getQuestions(TestPaper paper);
    void releasePaper(int id,int grade);
    AnswerSheet findSheet(User u,int paperId);

    double autoCorrect(int id);
}
