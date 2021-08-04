package com.company.dao;

import com.company.bean.AnswerSheet;
import com.company.utils.JdbcUtils;

import java.sql.*;


public class SheetDAO {


    public AnswerSheet findById(int id){
        AnswerSheet a = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getconn();
            String sql = "select * from sheets where id=?";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                a = new AnswerSheet();
                a.setStudentId(resultSet.getString("studentId"));
                a.setTestPaperId(resultSet.getInt("testPaperId"));
                a.setId(resultSet.getInt("id"));
                a.stringToAnswers(resultSet.getString("answers"));
                a.setNumOfAnswers(resultSet.getInt("numOfAnswers"));
                a.setSizeOfAnswers(resultSet.getInt("sizeOfAnswers"));
                a.setStatus(resultSet.getInt("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection,resultSet);
        }
        return a;
    }

    public void add(AnswerSheet sheet){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtils.getconn();
            String sql = "insert into sheets(studentId,testPaperId,id,answers,numOfAnswers,sizeOfAnswers,status)values(?,?,?,?,?,?,?);";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setString(1,sheet.getStudentId());
            preparedStatement.setInt(2,sheet.getTestPaperId());
            preparedStatement.setInt(3, sheet.getId());
            preparedStatement.setString(4,sheet.answersToString());
            preparedStatement.setInt(5,sheet.getNumOfAnswers());
            preparedStatement.setInt(6,sheet.getSizeOfAnswers());
            preparedStatement.setInt(7,sheet.getStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JdbcUtils.close(preparedStatement,connection);
        }

    }

    public void update(AnswerSheet sheet,int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtils.getconn();
            String sql = "delete from sheets where id=?;";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            add(sheet);
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
            String sql="select * from sheets";
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
CREATE TABLE IF NOT EXISTS sheets(
                                    studentId text null,
                                    testPaperId int null,
                                    id int null,
                                    answers text null,
                                    numOfAnswers int null,
                                    sizeOfAnswers int null,
                                    status int null
)DEFAULT CHARSET=utf8;
 */
/*
    private String studentId;
    private int testPaperId;
    private int id;
    private int[] answers=new int[50];
    private int numOfAnswers,sizeOfAnswers;
 */
