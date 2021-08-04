package com.company.dao;

import com.company.bean.Question;
import com.company.bean.User;
import com.company.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class QuestionDAO {


    public Question findById(int id){
        Question q = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getconn();
            String sql = "select * from questions where id=?";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                q = new Question();
                q.setId(resultSet.getInt("id"));
                q.setDescription(resultSet.getString("description"));
                q.setTitle(resultSet.getString("title"));
                q.setChoice(resultSet.getBoolean("isChoice"));
                q.setDifficulty(resultSet.getDouble("difficulty"));
                q.setScore(resultSet.getDouble("score"));
                q.setMultiple(resultSet.getBoolean("isMultiple"));
                q.setObjAnswer(resultSet.getInt("objAnswer"));
                q.setNumOfOptions(resultSet.getInt("numOfOptions"));
                q.setSubAnswer(resultSet.getString("subAnswer"));
                q.setShortAnswer(resultSet.getBoolean("isShortAnswer"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection,resultSet);
        }
        return q;
    }

    public void add(Question question){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtils.getconn();
            String sql = "insert into questions(id,description,title,isChoice,difficulty,score,isMultiple,objAnswer,numOfOptions,subAnswer,isShortAnswer)values(?,?,?,?,?,?,?,?,?,?,?);";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setInt(1,question.getId());
            preparedStatement.setString(2,question.getDescription());
            preparedStatement.setString(3,question.getTitle());
            preparedStatement.setBoolean(4,question.isChoice());
            preparedStatement.setDouble(5,question.getDifficulty());
            preparedStatement.setDouble(6, question.getScore());
            preparedStatement.setBoolean(7,question.isMultiple());
            preparedStatement.setInt(8,question.getObjAnswer());
            preparedStatement.setInt(9,question.getNumOfOptions());
            preparedStatement.setString(10,question.getSubAnswer());
            preparedStatement.setBoolean(11,question.isShortAnswer());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JdbcUtils.close(preparedStatement,connection);
        }

    }

    public void update(Question question,int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtils.getconn();
            String sql = "delete from questions where id=?;";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            add(question);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JdbcUtils.close(preparedStatement,connection);
        }

    }

    public void delete(int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtils.getconn();
            String sql = "delete from questions where id=?;";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JdbcUtils.close(preparedStatement,connection);
        }
    }

    public List<Question> findByType(int type){ //1:单选 2:多选 3:填空 4:简答
        List<Question> list=new ArrayList<Question>();
        Question q = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getconn();
            String sql;
            switch (type){
                case 1:
                    sql="select * from questions where isChoice=? and isMultiple=? order by score desc";
                    preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
                    preparedStatement.setBoolean(1,true);
                    preparedStatement.setBoolean(2,false);
                    break;
                case 2:
                    sql="select * from questions where isChoice=? and isMultiple=? order by score desc";
                    preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
                    preparedStatement.setBoolean(1,true);
                    preparedStatement.setBoolean(2,true);
                    break;
                case 3:
                    sql="select * from questions where isChoice=? and isShortAnswer=? order by score desc";
                    preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
                    preparedStatement.setBoolean(1,false);
                    preparedStatement.setBoolean(2,false);
                    break;
                case 4:
                    sql="select * from questions where isChoice=? and isShortAnswer=? order by score desc";
                    preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
                    preparedStatement.setBoolean(1,false);
                    preparedStatement.setBoolean(2,true);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                q = new Question();
                q.setId(resultSet.getInt("id"));
                q.setDescription(resultSet.getString("description"));
                q.setTitle(resultSet.getString("title"));
                q.setChoice(resultSet.getBoolean("isChoice"));
                q.setDifficulty(resultSet.getDouble("difficulty"));
                q.setScore(resultSet.getDouble("score"));
                q.setMultiple(resultSet.getBoolean("isMultiple"));
                q.setObjAnswer(resultSet.getInt("objAnswer"));
                q.setNumOfOptions(resultSet.getInt("numOfOptions"));
                q.setSubAnswer(resultSet.getString("subAnswer"));
                q.setShortAnswer(resultSet.getBoolean("isShortAnswer"));
                if(q.getDifficulty()==-1)continue;
                list.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection,resultSet);
        }
        return list;
    }

    public List<Question> findByDifficulty(double bottom,double top){
        Question q=null;
        List<Question> list=new ArrayList<Question>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getconn();
            String sql = "select * from questions where difficulty>? and difficulty<?";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setDouble(1,bottom);
            preparedStatement.setDouble(2,top);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                q = new Question();
                q.setId(resultSet.getInt("id"));
                q.setDescription(resultSet.getString("description"));
                q.setTitle(resultSet.getString("title"));
                q.setChoice(resultSet.getBoolean("isChoice"));
                q.setDifficulty(resultSet.getDouble("difficulty"));
                q.setScore(resultSet.getDouble("score"));
                q.setMultiple(resultSet.getBoolean("isMultiple"));
                q.setObjAnswer(resultSet.getInt("objAnswer"));
                q.setNumOfOptions(resultSet.getInt("numOfOptions"));
                q.setSubAnswer(resultSet.getString("subAnswer"));
                q.setShortAnswer(resultSet.getBoolean("isShortAnswer"));
                list.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection,resultSet);
        }
        return list;
    }

    public int getNum(){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try{
            connection=JdbcUtils.getconn();
            String sql="select * from questions";
            preparedStatement=connection.prepareStatement(sql);
            ResultSet res=preparedStatement.executeQuery();
            res.last();
            return res.getRow()+1;
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JdbcUtils.close(preparedStatement,connection);
        }
        return 0;
    }
    public int getValidId(){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        int v=-1;
        try{
            connection=JdbcUtils.getconn();
            String sql="select * from questions";
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
CREATE TABLE IF NOT EXISTS questions(
                                    id int null,
                                    description text null ,
                                    title text null ,
                                    isChoice boolean null ,
                                    difficulty double null ,
                                    score double null ,
                                    isMultiple boolean null ,
                                    objAnswer int null,
                                    numOfOptions int null,
                                    subAnswer text null,
                                    isShortAnswer boolean null
)DEFAULT CHARSET=utf8;
 */
/*
    private int id;
    private String description;
    private String title;
    private boolean isChoice;
    private double difficulty;
    private double score;

    private boolean isMultiple;
    private int objAnswer;
    private int numOfOptions;

    private String subAnswer;
    private boolean isShortAnswer;
 */
