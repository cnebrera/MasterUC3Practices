package com.cenebrera.uc3.tech.lesso10.marketDataProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.SessionID;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by alexvarela on 20/12/16.
 */
public class PriceReader
{
    private final Map<Long, Double> prices = new ConcurrentHashMap<Long, Double>();
    private final URL file;
    private final Timer timer;
    /** a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory */
    private final static Logger LOGGER = LoggerFactory.getLogger(PriceReader.class);

    public PriceReader(URL file)
    {
        this.file = file;
        this.timer = new Timer("Send Prices");
    }

    public  void init() throws URISyntaxException, FileNotFoundException
    {
        //Read the file with prices
        Scanner sc = new Scanner(new File(this.file.toURI()));

        //Read each line
        while (sc.hasNext())
        {
            //New Line
            String line = sc.nextLine();
            //get the two numbers
            String splitArray[] = line.split(" ");

            //the array must have 2 elements
            if (splitArray.length != 2)
            {
                throw new IllegalArgumentException("The file in bad formatted");
            }

            //Add the offset of time and price in the map
            this.prices.put(Long.valueOf(splitArray[0]), Double.valueOf(splitArray[1]));
        }
    }

    //Run the schedule of the incrementalRefresh.
    public void start(SessionID sessionID, String mdReqId)
    {
        long init = System.currentTimeMillis();
        for (Map.Entry<Long, Double> entry : this.prices.entrySet())
        {
            this.timer.schedule(new ReplayTask(sessionID, entry.getValue(), mdReqId) , new Date(init + entry.getKey() * 1000));
        }
    }


    /**
     * This class is used to send the messages
     *
     */
    private static class ReplayTask extends TimerTask
    {
        /** a org.slf4j.Logger with the instance of this class given by org.slf4j.LoggerFactory */
        private final static Logger LOGGER = LoggerFactory.getLogger(ReplayTask.class);

        /**
         * The identifier of the session
         */
        private final SessionID sessionID;

        /**
         * The price of the Incremental Refresh
         */
        private final Double price;

        /**
         * A String with the identifier of the market data request
         */
        private final String mdReqId;


        /**
         * Constructor of the ReplayTask
         *
         * @param sessionID a {@link SessionID} in order to send the messages
         * @param value a {@link Double} with the price.
         * @param mdReqId a {@link String} with the market data request Id.
         */
        public ReplayTask(SessionID sessionID, Double value, String mdReqId)
        {
            this.sessionID = sessionID;
            this.price = value;
            this.mdReqId = mdReqId;
        }

        public void run()
        {
            // TODO send incremental
        }
    }
}
