package com.duck.utils;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class TimeUtils {
    public static boolean isSame(Date date1, Date date2){
        return date1.getTime() == date2.getTime();
    }

    public static long getDifferenceBetween(Date date1, Date date2){
        return date2.getTime() - date1.getTime();
    }

    public static long getMillis(long time){
        return time % 1000;
    }

    /**
     *
     * @param time in millis...
     * @return seconds from time.
     */
    public static long getSeconds(long time){
        return TimeUnit.MILLISECONDS.toSeconds(time);
    }

    /**
     *
     * @param time in millis...
     * @return minutes from time.
     */
    public static long getMinutes(long time){
        return TimeUnit.MILLISECONDS.toMinutes(time);
    }

    /**
     *
     * @param time in millis...
     * @return hours from time.
     */
    public static long getHours(long time){
        return TimeUnit.MILLISECONDS.toHours(time);
    }

    public static String letterTimeFormat(long milliseconds)
    {
        StringBuilder sb = new StringBuilder();

        int millis = (int) (milliseconds % 1000);
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)));

        if (minutes > 0)
            sb.append(minutes).append("min. ");
        if (seconds > 0)
            sb.append(seconds).append("s. ");
        if (millis > 0)
            sb.append(millis).append("ms.");

        return sb.toString();
    }
}
