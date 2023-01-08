package com.flexingstudios.Common.player;

public class Leveling {
    private static final int BASE = 8000;
    private static final int GROWTH = 2000;
    private static final int MAX_LEVEL = 256;
    private static final int MAX_EXP;
    private static int[] EXP_TOTAL = new int[256];

    static {
        EXP_TOTAL[0] = 8000;
        for (int i = 1; i < 256; i++)
            EXP_TOTAL[i] = EXP_TOTAL[i - 1] + getExpToNextLevel(i + 1);
        MAX_EXP = EXP_TOTAL[255];
    }

    public static int getLevel(int exp) {
        int[] arr = EXP_TOTAL;
        if (exp < arr[0])
            return 1;
        if (exp > MAX_EXP)
            return 0;
        int left = 0;
        int right = 256;
        while (true) {
            int mid = (right + left) / 2;
            if (arr[mid] == exp)
                return mid + 2;
            if (right - left == 1)
                return left + 2;
            if (arr[mid] > exp) {
                right = mid;
                continue;
            }
            left = mid;
        }
    }

    public static int getTotalExp(int level) {
        if (level <= 1)
            return 0;
        return EXP_TOTAL[level - 2];
    }

    public static int getExpToNextLevel(int level) {
        if (level <= 0)
            return 8000;
        return 2000 * (level - 1) + 8000;
    }

    public static double getPercentageToNextLevel(int exp) {
        int lvl = getLevel(exp);
        return (exp - getTotalExp(lvl)) / getExpToNextLevel(lvl);
    }
}
