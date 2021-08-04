package com.company.dao;

import com.company.bean.User;
import com.company.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDAO {
    public User login(String id, String password){
        User u = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getconn();
            String sql = "select * from users where id=? and password=?";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            preparedStatement.setString(2,password);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                u = new User();
                u.setId(resultSet.getString("id"));
                u.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection,resultSet);
        }
        return u;
    }

    public User findById(String id){
        User u = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getconn();
            String sql = "select * from users where id=?";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                u = new User();
                u.setUsername(resultSet.getString("username"));
                u.setPassword(resultSet.getString("password"));
                u.setId(resultSet.getString("id"));
                u.setStudent(resultSet.getBoolean("isStudent"));
                u.stringToOwnedDocuments(resultSet.getString("ownedDocuments"));
                u.setNumOfOwnedDocuments(resultSet.getInt("numOfOwnedDocument"));
                u.setSizeOfOwnedDocuments(resultSet.getInt("sizeOfOwnedDocument"));
                u.setGrade(resultSet.getInt("grade"));
            }
        } catch (SQLException e) {e.printStackTrace();}
        finally{JdbcUtils.close(preparedStatement,connection,resultSet);}
        return u;
    }

    public void add(User user){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtils.getconn();
            String sql = "insert into users(username,password,id,isStudent,ownedDocuments,numOfOwnedDocument,sizeOfOwnedDocument,grade)values(?,?,?,?,?,?,?,?);";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2,user.getPassword());
            preparedStatement.setString(3,user.getId());
            preparedStatement.setBoolean(4,user.isStudent());
            preparedStatement.setString(5,user.ownedDocumentsToString());
            preparedStatement.setInt(6,user.getNumOfOwnedDocuments());
            preparedStatement.setInt(7,user.getSizeOfOwnedDocuments());
            preparedStatement.setInt(8,user.getGrade());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JdbcUtils.close(preparedStatement,connection);
        }

    }

    public void update(User user,String id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtils.getconn();
            String sql = "delete from users where id=?;";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            preparedStatement.executeUpdate();
            add(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JdbcUtils.close(preparedStatement,connection);
        }

    }

    public List<User> findStudentByGrade(int grade){
        User u = null;
        List<User> list=new ArrayList<User>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getconn();
            String sql = "select * from users where grade=? and isStudent=?";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setInt(1,grade);
            preparedStatement.setBoolean(2,true);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                u = new User();
                u.setUsername(resultSet.getString("username"));
                u.setPassword(resultSet.getString("password"));
                u.setId(resultSet.getString("id"));
                u.setStudent(resultSet.getBoolean("isStudent"));
                u.stringToOwnedDocuments(resultSet.getString("ownedDocuments"));
                u.setNumOfOwnedDocuments(resultSet.getInt("numOfOwnedDocument"));
                u.setSizeOfOwnedDocuments(resultSet.getInt("sizeOfOwnedDocument"));
                u.setGrade(resultSet.getInt("grade"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection,resultSet);
        }
        return list;
    }

    public List<User> findAllStudents(){
        User u = null;
        List<User> list=new ArrayList<User>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getconn();
            String sql = "select * from users where isStudent=?";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setBoolean(1,true);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                u = new User();
                u.setUsername(resultSet.getString("username"));
                u.setPassword(resultSet.getString("password"));
                u.setId(resultSet.getString("id"));
                u.setStudent(resultSet.getBoolean("isStudent"));
                u.stringToOwnedDocuments(resultSet.getString("ownedDocuments"));
                u.setNumOfOwnedDocuments(resultSet.getInt("numOfOwnedDocument"));
                u.setSizeOfOwnedDocuments(resultSet.getInt("sizeOfOwnedDocument"));
                u.setGrade(resultSet.getInt("grade"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection,resultSet);
        }
        return list;
    }


}
/*
CREATE TABLE IF NOT EXISTS users(
                                    username text null ,
                                    password text null ,
                                    id text null ,
                                    isStudent boolean null ,
                                    ownedDocuments text null ,
                                    numOfOwnedDocument int null ,
                                    sizeOfOwnedDocument int null ,
                                    grade int null
)DEFAULT CHARSET=utf8;
 */
