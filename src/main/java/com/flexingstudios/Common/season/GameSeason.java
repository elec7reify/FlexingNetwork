package com.flexingstudios.Common.season;

import java.time.LocalDate;
import java.time.ZoneId;

public abstract class GameSeason {
    public abstract String getTableSuffix();
    public abstract boolean isEnding();
    private static final ZoneId UTC = ZoneId.of("UTC");
    public static final GameSeason MONTHLY = new MonthlyGameSeason();

    private static class MonthlyGameSeason extends GameSeason {
        private long updated;
        private boolean ending;
        private String suffix;

        private MonthlyGameSeason() {
            throw new UnsupportedOperationException("This class cannot be instantiated");
        }

        private void update() {
            if (System.currentTimeMillis() - updated > 60000L) {
                updated = System.currentTimeMillis();
                LocalDate now = LocalDate.now(GameSeason.UTC);
                suffix = "_monthly_" + (now.getMonthValue() % 2 == 0 ? "a" : "b");
                ending = now.getMonth().length(now.isLeapYear()) - now.getDayOfMonth() <= 2;
            }
        }

        public String getTableSuffix() {
            update();
            return suffix;
        }

        public boolean isEnding() {
            update();
            return ending;
        }
    }
}
