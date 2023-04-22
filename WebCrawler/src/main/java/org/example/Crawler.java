package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.HashSet;

public class Crawler {
    HashSet<String>urlSet;
    int Max_Depth = 2;
    Crawler(){
        urlSet = new HashSet<String>();
    }
    public void getPageTextAndLinks(String url , int depth){
        if(urlSet.contains(url)){
            return;
        }
        if(depth >= Max_Depth){
            return;
        }
        if(urlSet.add(url)){
            System.out.println(url);
        }
        depth++;
        try {
            //we are getting the url page information in form of java document object
            Document document = Jsoup.connect(url).timeout(5000).get();
            //Indexer works starts here:


            Indexer indexer = new Indexer(document, url);//here we created an indexer object
            //the moment we create the indexer object it will establish the connection with database
            //and it will start saving our document title , link & text to our database

            System.out.println(document.title());
            //we will get all links that are present on the page
            //inside <a> tag of html we store our link & hyperlinks are stored using href attribute of a tag
            Elements availableLinksOnPage = document.select("a[href]");

            //for every link we will recursively call our getPageAndLinks function
            for (Element currentLink : availableLinksOnPage) {
                //getting the absolute link:-> convert element obj to string obj for url
                String availableUrl = currentLink.attr("abs:href");
                getPageTextAndLinks(availableUrl, depth);
            }
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }

    }
    public static void main(String[] args) {
        Crawler bot = new Crawler();
        bot.getPageTextAndLinks("https://www.javatpoint.com",0);
    }
}