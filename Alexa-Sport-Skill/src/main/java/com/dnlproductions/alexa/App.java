package com.dnlproductions.alexa;

import com.sun.syndication.feed.synd.SyndFeed;

/**
 * Main class to run the project
 *
 */
public class App {
    private static final String FEED_URL = "http://feeds.bbci.co.uk/sport/rss.xml?edition=int";

    public static void main( String[] args )
    {
        SyndFeed feed = RSSConnector.getInstance().createFeed(FEED_URL);
        RSSConnector.getInstance().printFeed(feed);
    }
}
