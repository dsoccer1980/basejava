package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;

public class DateUtil {

    public static final LocalDate NOW = of(3000, Month.JANUARY);

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    private DateUtil() {
    }
}
