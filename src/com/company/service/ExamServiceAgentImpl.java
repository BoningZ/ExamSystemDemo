package com.company.service;

import com.company.bean.*;
import com.company.exception.NameOrPasswordException;
import com.company.exception.PasswordNotMatchException;
import com.company.exception.UserExistException;
import com.company.server.Request;
import com.company.server.Response;
import com.company.utils.Consts;
import jdk.nashorn.internal.codegen.DumpBytecode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;


public class ExamServiceAgentImpl implements ExamService{
    String sessionId=null;
    @Override
    public User login(String id, String password, boolean isStudent) throws NameOrPasswordException {
        Response res=sendRequest("login",
                new Class[] { String.class,String.class,boolean.class },
                new Object[] { id, password,isStudent });
        if (res.isSuccess())
            return (User) res.getValue();
        if (res.getState() == Consts.ERR_NAME_OR_PASSWORD)
            throw new NameOrPasswordException(res.getMessage());
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public User register(String username, String id, String password, String repeatPassword, boolean isStudent, int grade) throws UserExistException, PasswordNotMatchException {
        Response res=sendRequest("register",
                new Class[]{String.class,String.class,String.class,String.class,boolean.class,int.class},
                new Object[]{username,id,password,repeatPassword,isStudent,grade});
        if (res.isSuccess())
            return (User) res.getValue();
        if (res.getState() == Consts.ERR_USER_EXIST)
            throw new UserExistException(res.getMessage());
        if(res.getState()==Consts.ERR_PASSWORD_NOT_MATCH)
            throw new PasswordNotMatchException(res.getMessage());
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public List<TestPaper> getDoneExam(String userId){
        Response res=sendRequest("getDoneExam",
                new Class[]{String.class},
                new Object[]{userId});
        if(res.isSuccess())
            return (List<TestPaper>)res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public List<TestPaper> getTodoExam(String userId){
        Response res=sendRequest("getTodoExam",
                new Class[]{String.class},
                new Object[]{userId});
        if(res.isSuccess())
            return (List<TestPaper>)res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public Object[][] getDoneInfo(String userId){
        Response res=sendRequest("getDoneInfo",
                new Class[]{String.class},
                new Object[]{userId});
        if(res.isSuccess())
            return (Object[][]) res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public Object[][] getTableData(TestPaper paper) {
        Response res=sendRequest("getTableData",
                new Class[]{TestPaper.class},
                new Object[]{paper});
        if(res.isSuccess())
            return (Object[][]) res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public List<TestPaper> getReleased(String userId){
        Response res=sendRequest("getReleased",
                new Class[]{String.class},
                new Object[]{userId});
        if(res.isSuccess())
            return (List<TestPaper>)res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public List<TestPaper> getUnreleased(String userId){
        Response res=sendRequest("getUnreleased",
                new Class[]{String.class},
                new Object[]{userId});
        if(res.isSuccess())
            return (List<TestPaper>)res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public List<AnswerSheet> getToCorrect(TestPaper paper) {
        Response res=sendRequest("getToCorrect",
                new Class[]{TestPaper.class},
                new Object[]{paper});
        if(res.isSuccess())
            return (List<AnswerSheet>)res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public List<AnswerSheet> getCorrected(TestPaper paper) {
        Response res=sendRequest("getCorrected",
                new Class[]{TestPaper.class},
                new Object[]{paper});
        if(res.isSuccess())
            return (List<AnswerSheet>)res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public Question createQuestion(int type) {
        Response res=sendRequest("createQuestion",
                new Class[]{int.class},
                new Object[]{type});
        if(res.isSuccess())
            return (Question)res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public TestPaper createPaper(User u){
        Response res=sendRequest("createPaper",
                new Class[]{User.class},
                new Object[]{u});
        if(res.isSuccess())
            return (TestPaper) res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public Answer createAnswer(int questionId) {
        Response res=sendRequest("createAnswer",
                new Class[]{int.class},
                new Object[]{questionId});
        if(res.isSuccess())
            return (Answer) res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public List<Question> screenQuestion(boolean singleChoice, boolean multiChoice, boolean fillBlank, boolean shortAns) {
        Response res=sendRequest("screenQuestion",
                new Class[]{boolean.class,boolean.class,boolean.class,boolean.class},
                new Object[]{singleChoice,multiChoice,fillBlank,shortAns});
        if(res.isSuccess())
            return (List<Question>)res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public void deleteQuestion(int id) {
        Response res=sendRequest("deleteQuestion",
                new Class[]{int.class},
                new Object[]{id});
        if(res.isSuccess())return;
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public Question findQuestion(int id) {
        Response res=sendRequest("findQuestion",
                new Class[]{int.class},
                new Object[]{id});
        if(res.isSuccess())
            return (Question)res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public TestPaper findPaper(int id){
        Response res=sendRequest("findPaper",
                new Class[]{int.class},
                new Object[]{id});
        if(res.isSuccess())
            return (TestPaper) res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public Answer findAnswer(int id) {
        Response res=sendRequest("findAnswer",
                new Class[]{int.class},
                new Object[]{id});
        if(res.isSuccess())
            return (Answer) res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public void updateQuestion(int id, Question q) {
        Response res=sendRequest("updateQuestion",
                new Class[]{int.class,Question.class},
                new Object[]{id,q});
        if(res.isSuccess())return;
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public void updatePaper(TestPaper paper){
        Response res=sendRequest("updatePaper",
                new Class[]{TestPaper.class},
                new Object[]{paper});
        if(res.isSuccess())return;
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public void updateSheet(AnswerSheet sheet) {
        Response res=sendRequest("updateSheet",
                new Class[]{AnswerSheet.class},
                new Object[]{sheet});
        if(res.isSuccess())return;
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public void updateAnswer(Answer answer) {
        Response res=sendRequest("updateAnswer",
                new Class[]{Answer.class},
                new Object[]{answer});
        if(res.isSuccess())return;
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public TestPaper generatePaper(int numOfSingle, int numOfMulti, int numOfFill, int numOfShort, double scoreOfSingle, double scoreOfMulti, double scoreOfFill, double scoreOfShort,double diff) {
        Response res=sendRequest("generatePaper",
                new Class[]{int.class,int.class,int.class,int.class,double.class,double.class,double.class,double.class,double.class},
                new Object[]{numOfSingle,numOfMulti,numOfFill,numOfShort,scoreOfSingle,scoreOfMulti,scoreOfFill,scoreOfShort,diff});
        if(res.isSuccess())return (TestPaper)res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public int getNumOfType(TestPaper paper, int type) {
        Response res=sendRequest("getNumOfType",
                new Class[]{TestPaper.class,int.class},
                new Object[]{paper,type});
        if(res.isSuccess())return (int)res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public double getScoreOfType(TestPaper paper, int type) {
        Response res=sendRequest("getScoreOfType",
                new Class[]{TestPaper.class,int.class},
                new Object[]{paper,type});
        if(res.isSuccess())return (double)res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public double getDiffOfType(TestPaper paper, int type) {
        Response res=sendRequest("getDiffOfType",
                new Class[]{TestPaper.class,int.class},
                new Object[]{paper,type});
        if(res.isSuccess())return (double)res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public List<Question> getQuestions(TestPaper paper) {
        Response res=sendRequest("getQuestions",
                new Class[]{TestPaper.class},
                new Object[]{paper});
        if(res.isSuccess())return (List<Question>)res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public void releasePaper(int id, int grade) {
        Response res=sendRequest("releasePaper",
                new Class[]{int.class,int.class},
                new Object[]{id,grade});
        if(res.isSuccess())return;
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public AnswerSheet findSheet(User u, int paperId) {
        Response res=sendRequest("findSheet",
                new Class[]{User.class,int.class},
                new Object[]{u,paperId});
        if(res.isSuccess())return (AnswerSheet) res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    @Override
    public double autoCorrect(int id) {
        Response res=sendRequest("autoCorrect",
                new Class[]{int.class},
                new Object[]{id});
        if(res.isSuccess())return (double) res.getValue();
        throw new RuntimeException(res.getMessage());
    }

    private Response sendRequest(String method, Class[] argTypes, Object[] args) {
        String host = Consts.SERVER_IP;
        int port = Consts.SERVER_PORT;
        try {
            Socket socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Request request = new Request(sessionId, method, argTypes, args);
            out.writeObject(request);
            out.flush();
            Response res = (Response) in.readObject();
            System.out.println("Response:" + res);
            sessionId = res.getSessionId();
            socket.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Õ¯¬Á¡¨Ω”π ’œ!", e);
        }
    }
}
