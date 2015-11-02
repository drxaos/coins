package com.github.drxaos.coins.utils;

import com.github.drxaos.coins.application.factory.Component;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class DateUtil {
    public Date now() {
        return new GregorianCalendar().getTime();
    }

    public Date clearTime(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public Date setTime(Date date, int hour, int minute, int second) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public String format(Date date, String format) {
        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("ru"));
        dfs.setMonths(months);
        SimpleDateFormat sdf = new SimpleDateFormat(format, dfs);
        return sdf.format(date);
    }

    public String format(Date date) {
        return format(date, "dd MMMM yyyy 'г.'");
    }

    public String formatToJson(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return formatter.format(date);
    }

    public String formatDateTime(Date date) {
        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("ru"));
        dfs.setMonths((String[]) months);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss, dd MMMM yyyy 'г.'", dfs);
        return sdf.format(date);
    }

    public String formatDateTimeShort(Date date) {
        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("ru"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", dfs);
        return sdf.format(date);
    }

    public String formatDateShort(Date date) {

        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("ru"));
        dfs.setMonths(months);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy", dfs);
        return sdf.format(date);
    }

    public String formatForZone(Date date, String zone) {
        DateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy zzz");
        TimeZone central = TimeZone.getTimeZone(zone);
        formatter.setTimeZone(central);
        return formatter.format(date);
    }

    public Date parseDate(String date) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.parse(date);
    }

    public Date parseDateTime(String date, String time) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        return formatter.parse(time + " " + date);
    }

    public Date parseTime(String time) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.parse(time);
    }

    public Date parseFromJson(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return formatter.parse(date);
    }

    public boolean timeIsBefore(Date d1, Date d2) {
        DateFormat f = new SimpleDateFormat("HH:mm:ss.SSS");
        return f.format(d1).compareTo(f.format(d2)) < 0;
    }

    public String formatDayPeriod(Integer period) {
        if (period == null) {
            return "";
        }
        int p10 = period % 10;
        if (p10 == 1 && period != 11) {
            return String.valueOf(period) + " день";
        }

        if (p10 >= 2 && p10 <= 4) {
            return String.valueOf(period) + " дня";
        }

        return String.valueOf(period) + " дней";
    }

    public Integer daysInMonth(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        int dayInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return dayInMonth;
    }

    public Integer daysInMonth() {
        return daysInMonth(now());
    }

    public int getDayOfMonth(Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public final static String[] months = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
}
