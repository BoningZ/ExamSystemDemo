package com.company.dao;

import com.company.bean.TestPaper;
import com.company.utils.JdbcUtils;

import java.sql.*;


public class PaperDAO {


    public TestPaper findById(int id){
        TestPaper p = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getconn();
            String sql = "select * from papers where id=?";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                p = new TestPaper();
                p.setId(resultSet.getInt("id"));
                p.setTitle(resultSet.getString("title"));
                p.setStatus(resultSet.getInt("status"));
                p.setTimeLimit(resultSet.getInt("timeLimit"));
                p.stringToQuestions(resultSet.getString("questions"));
                p.setNumOfQuestions(resultSet.getInt("numOfQuestions"));
                p.setSizeOfQuestions(resultSet.getInt("sizeOfQuestions"));
                p.stringToAnswerSheets(resultSet.getString("answerSheets"));
                p.setNumOfAnswerSheets(resultSet.getInt("numOfAnswerSheets"));
                p.setSizeOfAnswerSheets(resultSet.getInt("sizeOfAnswerSheets"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection,resultSet);
        }
        return p;
    }

    public void add(TestPaper paper){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtils.getconn();
            String sql = "insert into papers(id,title,status,timeLimit,questions,numOfQuestions,sizeOfQuestions,answerSheets,numOfAnswerSheets,sizeOfAnswerSheets)values(?,?,?,?,?,?,?,?,?,?);";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setInt(1, paper.getId());
            preparedStatement.setString(2,paper.getTitle());
            preparedStatement.setInt(3,paper.getStatus());
            preparedStatement.setInt(4,paper.getTimeLimit());
            preparedStatement.setString(5,paper.questionsToString());
            preparedStatement.setInt(6,paper.getNumOfQuestions());
            preparedStatement.setInt(7,paper.getSizeOfQuestions());
            preparedStatement.setString(8,paper.answerSheetsToString());
            preparedStatement.setInt(9,paper.getNumOfAnswerSheets());
            preparedStatement.setInt(10,paper.getSizeOfAnswerSheets());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JdbcUtils.close(preparedStatement,connection);
        }

    }

    public void update(TestPaper paper,int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtils.getconn();
            String sql = "delete from papers where id=?;";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            add(paper);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JdbcUtils.close(preparedStatement,connection);
        }

    }
    public int getValidId(){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        int v=-1;
        try{
            connection=JdbcUtils.getconn();
            String sql="select * from papers";
            preparedStatement=connection.prepareStatement(sql);
            ResultSet res=preparedStatement.executeQuery();
            while(res.next())
                v=Math.max(v,res.getInt("id"));
            if(v!=-1)return v+1;
            return 1;
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JdbcUtils.close(preparedStatement,connection);
        }
        return 1;
    }

}
/*
CREATE TABLE IF NOT EXISTS papers(
                                    id int null,
                                    title text null,
                                    status int null,
                                    timeLimit int null,
                                    questions text null,
                                    numOfQuestions int null,
                                    sizeOfQuestions int null,
                                    answerSheets text null,
                                    numOfAnswerSheets int null,
                                    sizeOfAnswerSheets int null
)DEFAULT CHARSET=utf8;
 */
/*
    private int id;
    private String title;
    private int status;
    private int timeLimit;
    private int[] questions=new int[50];
    private int numOfQuestions,sizeOfQuestions;
    private int[] answerSheets=new int[50];
    private int numOfAnswerSheets,sizeOfAnswerSheets;
 */
