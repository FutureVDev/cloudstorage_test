package com.cloud.storage.server;

import java.sql.*;

import static com.cloud.storage.common.MainTest.connectToDb;

public class SqlHandler {
    static Connection connection;
    static Statement statement;
    static PreparedStatement preparedStatement = null;

//    public SqlHandler(){
//
//    }
    public int removeSessionForUser(String userName){
        int success = 0;
        try {
            connectToDb();
            preparedStatement = connection.prepareStatement("update users set sessionId = null  where user_name = ?");
            preparedStatement.setString(1, userName);
            success = preparedStatement.executeUpdate();
            return success;
        }catch (Exception e) {
            System.out.println(e);
        } finally {
            disconnectFromDb();
        }
        return success;
    }

    public int setSessionForUser(String sessionId, String userName){
        int success = 0;
        try {
            connectToDb();
            preparedStatement = connection.prepareStatement("update users set sessionId = ? where user_name = ?");
            preparedStatement.setString(1, sessionId);
            preparedStatement.setString(2, userName);
            success = preparedStatement.executeUpdate();
            return success;
        }catch (Exception e) {
            System.out.println(e);
        } finally {
            disconnectFromDb();
        }
        return success;
    }

    public String getPasswordFromUser(String userName){
        try {
            connectToDb();
            preparedStatement = connection.prepareStatement("select password from users where user_name = ?");
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                return resultSet.getString(1);
            }
        }catch (Exception e) {
            System.out.println(e);
        } finally {
            disconnectFromDb();
        }
        return "fail";
    }


    public String getUserFromSession(String sessionId){
        try {
            connectToDb();
            preparedStatement = connection.prepareStatement("select user_name from users where sessionId = ?");
            preparedStatement.setString(1, sessionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                return resultSet.getString(1);
            }
        }catch (Exception e) {
            System.out.println(e);
        } finally {
            disconnectFromDb();
        }
        return "fail";
    }

    public static void connectToDb() throws Exception{
        Class.forName("org.sqlite.JDBC");

        connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/1/Desktop/cloudServerFileHolder/db/cloud_server");
        statement = connection.createStatement();
    }

    public static void disconnectFromDb(){
        try {
            connection.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
