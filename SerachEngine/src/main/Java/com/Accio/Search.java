package com.Accio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/Search")
public class Search extends HttpServlet {
    protected  void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //getting the keyword from the frontend
        String keyWord = request.getParameter("keyword");
        //get the connection first to the database
        Connection connection = DatabaseConnection.getConnection();
        try {
            //store the Query into the history table
            //for that we need to prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement("Insert into history values(?, ?);");
            preparedStatement.setString(1, keyWord);
            preparedStatement.setString(2, "http://localhost:8080/SerachEngine/Search?keyWord="+keyWord);
            //here now prepare the query to execute it
            preparedStatement.executeUpdate();
            //run the database Query for Ranking Algorithm
            //and store it to the result set
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT pagetitle, pagelink, (length(lower(pageText)) - length(replace(lower(pageText), '"+keyWord.toLowerCase()+"', '')))/length('"+keyWord.toLowerCase()+"') as countoccurence from pages order by countoccurence desc limit 30;");
            //make a arraylist of search result type
            ArrayList<SearchResult>results = new ArrayList<>();
            //now set the page link and title to the search result
            while(resultSet.next()){
                SearchResult searchResult = new SearchResult();
                //set the page title to the search result
                searchResult.setTitle(resultSet.getString("pageTitle"));
                //set the page link to the search result
                searchResult.setLink(resultSet.getString("pageLink"));
                //add it to the results arraylist
                results.add(searchResult);
            }

            for(SearchResult searchResult : results){
                System.out.println(searchResult.getTitle() + "\n" + searchResult.getLink() + "\n");
            }
            //set the attribute for send the reult to the frontend
            request.setAttribute("results", results);
            //now forward the request to the front end part
            request.getRequestDispatcher("search.jsp").forward(request, response);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        catch (ServletException servletException){
            servletException.printStackTrace();
        }
    }
}