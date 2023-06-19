package com.flexingstudios.flexingnetwork.api.util;

public class ParsedTime {
    public final int millis;
    public final int seconds;
    public final int minutes;
    public final int hours;
    public final int days;

    public ParsedTime(long inp) {
        this.millis = (int) (inp % 1000L);
        this.seconds = (int)((inp /= 1000L) % 60L);
        this.minutes = (int)((inp /= 60L) % 60L);
        this.hours = (int)((inp /= 60L) % 24L);
        this.days = (int) (inp /= 24L);
    }

    public static String numToString(int num, int length) {
        String str = num + "";
        while (str.length() < length)
            str = "0" + str;

        if (str.length() > length)
            str = str.substring(0, length);

        return str;
    }

    public String format() {
        StringBuilder sb = new StringBuilder();
        if (days != 0)
            sb.append(days).append(" д. ");

        if (hours != 0)
            sb.append(hours).append(" ч. ");

        if (minutes != 0)
            sb.append(minutes).append(" м. ");

        if (seconds != 0)
            sb.append(seconds).append(" с.");

        return sb.toString().trim();
    }
}
