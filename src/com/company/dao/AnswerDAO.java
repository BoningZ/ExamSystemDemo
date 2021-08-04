package com.company.dao;

import com.company.bean.Answer;
import com.company.utils.JdbcUtils;

import java.sql.*;


public class AnswerDAO {


    public Answer findById(int id){
        Answer a = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getconn();
            String sql = "select * from answers where id=?";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                a = new Answer();
                a.setQuestionId(resultSet.getInt("questionId"));
                a.setId(resultSet.getInt("id"));
                a.setScore(resultSet.getDouble("score"));
                a.setObjAnswer(resultSet.getInt("objAnswer"));
                a.setSubAnswer(resultSet.getString("subAnswer"));
                a.setCorrected(resultSet.getBoolean("isCorrected"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection,resultSet);
        }
        return a;
    }

    public void add(Answer answer){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtils.getconn();
            String sql = "insert into answers(questionId,id,score,objAnswer,subAnswer,isCorrected)values(?,?,?,?,?,?);";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setInt(1,answer.getQuestionId());
            preparedStatement.setInt(2,answer.getId());
            preparedStatement.setDouble(3, answer.getScore());
            preparedStatement.setInt(4,answer.getObjAnswer());
            preparedStatement.setString(5,answer.getSubAnswer());
            preparedStatement.setBoolean(6,answer.isCorrected());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JdbcUtils.close(preparedStatement,connection);
        }

    }

    public void update(Answer answer,int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtils.getconn();
            String sql = "delete from answers where id=?;";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            add(answer);
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
            String sql="select * from answers";
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
CREATE TABLE IF NOT EXISTS answers(
                                    questionId int null,
                                    id int null,
                                    score double null ,
                                    objAnswer int null,
                                    subAnswer text null,
                                    isCorrected boolean null
)DEFAULT CHARSET=utf8;
 */
/*
    private int questionId;
    private int id;
    private double score;
    private int objAnswer;
    private String subAnswer;
    private boolean isCorrected;
 */
