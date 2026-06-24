package ru.iteco.fmhandroid.ui.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static String getToday() {
        return LocalDate.now().format(FORMATTER);
    }

    public static String getTomorrow() {
        return LocalDate.now().plusDays(1).format(FORMATTER);
    }
    public static String getAfterTomorrow() {
        return LocalDate.now().plusDays(2).format(FORMATTER);
    }

    public static String getDateDaysAgo(int days) {
        return LocalDate.now().minusDays(days).format(FORMATTER);
    }
}