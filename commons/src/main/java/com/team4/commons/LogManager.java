package com.team4.commons;

import java.util.concurrent.TimeUnit;

public class LogManager {

    public static void print(String msg, long zeroTime) {
        StringBuilder sb = new StringBuilder(formatElapsedTime(System.currentTimeMillis(), zeroTime));
        sb.append(msg);
        System.out.println(sb.toString());
    }

    private static String formatElapsedTime(long milliTime, long zeroTime) {
        long elapsedTime = milliTime - zeroTime;
        long elapsedSeconds = TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.MILLISECONDS);
        long s = elapsedSeconds % 60;
        long m = ((elapsedSeconds - s) % 3600) / 60;
        long h = (elapsedSeconds - (elapsedSeconds - s) % 3600) / 60;
        return String.format("[%02d:%02d:%02d]  ", h, m, s);
    }
}
