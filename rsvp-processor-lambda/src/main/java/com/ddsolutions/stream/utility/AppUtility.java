package com.ddsolutions.stream.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class AppUtility {
    private static final Logger LOGGER = LogManager.getLogger(AppUtility.class);

    public static String getTimeStampToDate(String timeStamp) {
        Instant time = Instant.parse(timeStamp);

        Date date = new Date(time.toEpochMilli());
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String dateFormat = format.format(date);
        LOGGER.debug("Converted {} to {} ", timeStamp, dateFormat);
        return dateFormat;
    }

    public static String getFormattedDate(String date) {
        Date parse = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            parse = dateFormat.parse(date);
            LOGGER.debug("Converted {} to {} ", date, parse);
        } catch (ParseException e) {
            LOGGER.error("Date conversion exception", e);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ");
        String formattedDate = formatter.format(parse);
        LOGGER.debug("converted {} to {} ", parse, formattedDate);
        return formattedDate;
    }
}
