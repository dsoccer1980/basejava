package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

    public static final LocalDate NOW = of(3000, Month.JANUARY);

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    private DateUtil() {
    }

    public static LocalDate parse(String date) {
        YearMonth yearMonth = YearMonth.parse(date, DATE_FORMATTER);
        return LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
    }
}
