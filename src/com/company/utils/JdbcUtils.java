package com.company.utils;

import java.sql.*;


public class JdbcUtils {
    private static String url = "jdbc:mysql://localhost:3306/MySQL?useUnicode=true&characterEncoding=utf8";
    private static String user = "root";
    private static String password = "zbn123ZBN456";
    private static String dv = "com.mysql.jdbc.Driver";

    static{

        try {
            Class.forName(dv);
        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static Connection getconn() throws SQLException {
        Connection connection = DriverManager.getConnection(url,user,password);
        return connection;

    }

    public static void close(Statement statement, Connection connection){
        if(statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(PreparedStatement preparedStatement, Connection connection, ResultSet result) {
        if(preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(result != null){
            try {
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
