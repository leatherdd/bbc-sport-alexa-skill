package com.dnlproductions.alexa;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class.
 *
 * Created by leatherd on 22/12/2016.
 */
public class RSSConnector {
    private static RSSConnector rssConnector = new RSSConnector();


    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private RSSConnector() {}

    /* Static 'instance' method */
    public static RSSConnector getInstance() {
        return rssConnector;
    }

    public SyndFeed createFeed(String feedUrl) {
        SyndFeed feed = null;

        try {
            URL url = new URL(feedUrl);
            SyndFeedInput input = new SyndFeedInput();
            feed = input.build(new XmlReader(url));
            System.out.println("You're listening to " + feed.getTitle());
            return feed;

        } catch (MalformedURLException e) {
            System.out.println("The URL seems to be malformed");
            e.getMessage();
        } catch (IOException e) {
            System.out.println("There was an IO exception");
            e.getMessage();
        } catch (FeedException e) {
            System.out.println("There was an error building the feed");
            e.getMessage();
        }

        return feed;
    }

    public ArrayList<String> getSetNumberOfItems(SyndFeed feed, int number) {
        ArrayList<String> list = new ArrayList<String>();

        if(number != 0) {

        }

        return list;
    }

    public void printFeed(SyndFeed feed) {
        List<SyndEntry> entries = feed.getEntries();

        for (SyndEntry entry : entries) {
            System.out.println();
            System.out.println("-------------------------------------------------------");
            System.out.println(entry.getTitle());
            System.out.println(entry.getDescription().getValue());
            System.out.println("-------------------------------------------------------");
            System.out.println();
        }
    }

}
