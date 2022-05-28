package com.flexingstudios.Commons;

public class F {
    private static final char[] magnitudes = new char[] { 'k', 'M', 'G', 'T', 'P', 'E' };

    public static String formatBytes(long bytes) {
        if (bytes < 1024L)
            return bytes + " B";
        int exp = (int)(Math.log(bytes) / Math.log(1024.0D));
        String pre = "KMGTPE".charAt(exp - 1) + "iB";
        return formatFloat((float)(bytes / Math.pow(1024.0D, exp)), 1) + " " + pre;
    }

    public static String trimNumber(long number) {
        String ret;
        if (number >= 0L) {
            ret = "";
        } else {
            if (number <= -9200000000000000000L)
                return "-9.2E";
            ret = "-";
            number = -number;
        }
        if (number < 1000L)
            return ret + number;
        for (int i = 0;; i++) {
            if (number < 10000L && number % 1000L >= 100L)
                return ret + (number / 1000L) + '.' + (number % 1000L / 100L) + magnitudes[i];
            number /= 1000L;
            if (number < 1000L)
                return ret + number + magnitudes[i];
        }
    }

    public static String formatNumberDelimited(long number, char delimiter) {
        if (number <= 999L)
            return String.valueOf(number);
        char[] chars = String.valueOf(number).toCharArray();
        StringBuilder sb = new StringBuilder(chars.length + (chars.length - 1) / 3);
        int begin = chars.length % 3;
        sb.append(chars, 0, begin);
        for (int i = begin; i < chars.length; i += 3) {
            if (i != 0)
                sb.append(delimiter);
            sb.append(chars, i, (chars.length - i >= 3) ? 3 : (chars.length - i));
        }
        return sb.toString();
    }

    public static String formatFloat(float num, int pr) {
        StringBuilder sb = new StringBuilder();
        sb.append((int)num);
        sb.append('.');
        for (int i = 0; i < pr; i++) {
            num *= 10.0F;
            sb.append((int)num % 10);
        }
        return sb.toString();
    }

    public static String formatSecondsShort(int seconds) {
        if (seconds < 0)
            return "0 мин";
        if (seconds <= 3540)
            return plurals((int)Math.ceil((seconds / 60.0F)), "минута", "минуты", "минут");
        if (seconds <= 82800)
            return plurals((int)Math.ceil((seconds / 3600.0F)), "час", "часа","часов");
        if (seconds <= 2505600)
            return plurals((int)Math.ceil((seconds / 86400.0F)), "день", "дня", "дней");
        if (seconds <= 28512000)
            return plurals((int)Math.ceil((seconds / 2592000.0F)), "месяц","месяца", "месяцев");
        return plurals((int)Math.ceil((seconds / 3.1104E7F)), "год","года", "лет");
    }

    public static String plurals(int n, String form1, String form2, String form3) {
        int orig = n;
        if (n == 0)
            return orig + " " + form3;
        n = Math.abs(n) % 100;
        if (n > 10 && n < 20)
            return orig + " " + form3;
        n %= 10;
        if (n > 1 && n < 5)
            return orig + " " + form2;
        if (n == 1)
            return orig + " " + form1;
        return orig + " " + form3;
    }
}
