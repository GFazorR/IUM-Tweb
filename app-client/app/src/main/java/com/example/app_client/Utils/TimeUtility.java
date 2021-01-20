package com.example.app_client.Utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TimeUtility {

    private static final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.ITALY).getFirstDayOfWeek();
    private static final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String TIME_FORMAT = "EEE dd";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTime getFirstDayOfWeek() {
        return LocalDateTime.now(Clock.systemDefaultZone())
                .with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
                .withHour(0).withMinute(0).withSecond(0).withNano(0); // first day
    }

    public static LocalDateTime getLastDayOfWeek() {
        return  LocalDateTime.now(Clock.systemDefaultZone())
                .with(TemporalAdjusters.nextOrSame(lastDayOfWeek))
                .withHour(0).withMinute(0).withSecond(0).withNano(0); // last day
    }

    public static String formatTimestamp(LocalDateTime dateTime, String formattingString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formattingString);
        return formatter.format(dateTime);
    }

    public static String formatWeekday(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        return formatter.format(date);
    }

}
