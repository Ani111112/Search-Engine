package com.Accio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/History")
public class History extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        Connection connection = DatabaseConnection.getConnection();
        try{
            //this Query view the all history
           ResultSet resultSet = connection.createStatement().executeQuery("Select * from history;");
            ArrayList<HistoryResult>results = new ArrayList<HistoryResult>();
            while(resultSet.next()){
                HistoryResult historyResult = new HistoryResult();
                //set the keyword into the historyResult keyword
                historyResult.setKeyWord(resultSet.getString("keyword"));
                //set the keyword into the historyResult link
                historyResult.setLink(resultSet.getString("link"));
                //add the result to the historys arraylist
                results.add(historyResult);
            }
            //forwarding the result arrayList to the frontend
            request.setAttribute("results", results);
            request.getRequestDispatcher("history.jsp").forward(request, response);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
