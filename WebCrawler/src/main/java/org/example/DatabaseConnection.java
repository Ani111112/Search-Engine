package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    static Connection connection = null;
    public static Connection getConnection(){
        //It means we already have a connection
        //set Every time connction is very costly operation so we are useing one connecton for every time crawler hit
        if(connection != null)return connection;
        //now give the database details for build a connection
        String user = "root";
        String pwd = "Aniruddha@123";
        String databaseName = "searchenginedatabase";
        //this is a overloaded method of getConnection
        return getConnection(user, pwd, databaseName);
    }

    private static Connection getConnection(String user, String pwd, String databaseName) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/"+databaseName+"?user="+user+"&password="+pwd);
        }catch (ClassNotFoundException | SQLException classNotFoundException){
            classNotFoundException.printStackTrace();
        }
        return connection;
    }
}
