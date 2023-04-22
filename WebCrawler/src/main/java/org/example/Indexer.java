package org.example;

import org.jsoup.nodes.Document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Indexer {
    static Connection connection = null;
    Indexer(Document document , String url){
        //selecting the  important elements of document object:
        String title = document.title();
        String link = url;
        String text = document.text();

        //save these important elements to database:

        try {
            connection = DatabaseConnection.getConnection();

            //we will be using preparedStatement class of jdbc library  for making an insert query in our database
            //we will be inserting title , link & text inside our page table in Mysql using java code;
            PreparedStatement preparedStatement = connection.prepareStatement("Insert into pages values(?, ?, ?); ");
            //we are preparing the query:
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, link);
            preparedStatement.setString(3, text);
            //the statement is prepared now
            //we will execute the object of this prepared statement
            preparedStatement.executeUpdate();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

    }
}