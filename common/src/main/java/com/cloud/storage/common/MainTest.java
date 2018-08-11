package com.cloud.storage.common;

import com.mysql.jdbc.exceptions.MySQLDataException;

import java.io.File;
import java.sql.*;
import java.util.Arrays;
import java.util.UUID;

public class MainTest {
        static Connection connection;
        static Statement statement;
        static PreparedStatement preparedStatement = null;
//        static ResultSet resultSet = null;
    public static void main(String[] args) {
//        File fileDir = new File("C:/Users/1/Desktop/cloudServerFileHolder/client/");
//        File[] fileList = fileDir.listFiles();
//        System.out.println(fileList[1].toString());
        String uniqueID = UUID.randomUUID().toString();
        System.out.println(uniqueID);

        try {
            connectToDb();
            int success;
//            preparedStatement = connection.prepareStatement("update users set sessionId = ? where user_name = ?");
//            preparedStatement.setString(1, "carlSession");
//            preparedStatement.setString(2, "carll");
//            success = preparedStatement.executeUpdate();
//            System.out.println(success);
            preparedStatement = connection.prepareStatement("select password from users where user_name = ?");
            preparedStatement.setString(1, "carll");
            ResultSet resultSet = preparedStatement.executeQuery();
//            ResultSet resultSet = statement.executeQuery("select * from users;");
            while (resultSet.next()){
                System.out.println(resultSet.getString(1));
            }
        }catch (Exception e) {
            System.out.println(e);
        } finally {
            disconnectFromDb();
        }

//            statement = connection.createStatement();
//            resultSet = statement.executeQuery("select * from cloud_server.users");
    }


    private void writeResultSet(ResultSet resultSet) throws Exception {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            String user = resultSet.getString("user");
            String password = resultSet.getString("password");
            String sessionId = resultSet.getString("sessionId");
//            Date date = resultSet.getDate("datum");
//            String comment = resultSet.getString("comments");
            System.out.println("User: " + user);
            System.out.println("Website: " + password);
            System.out.println("sessionId: " + sessionId);
//            System.out.println("Date: " + date);
//            System.out.println("Comment: " + comment);
        }
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
//            System.out.printf(e);
        }
    }
    }

