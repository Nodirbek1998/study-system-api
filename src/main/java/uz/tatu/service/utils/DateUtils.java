package uz.tatu.service.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static LocalDate getSysLocaleDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.now();
        return localDate;
    }

    public static LocalDate stringToLocalDate(String date) {
        if (!date.equals("")) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate localDate = LocalDate.parse(date, dtf);
            return localDate;
        }
        return null;
    }

    public static String LocalDateTimeToString(LocalDateTime date) {
        if (date != null) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            return date.format(dtf);
        }
        return null;
    }

    public static String parseLocalDateByFormat(LocalDate value, String format) {
        if (value != null) {
            return value.format(DateTimeFormatter.ofPattern(format));
        }
        return null;
    }

    public static String parseTimeByFormat(LocalTime value, String format) {
        if (value != null) {
            return value.format(DateTimeFormatter.ofPattern(format));
        }
        return null;
    }

    public static String parseLocalDateWithDefaultValue(LocalDate value, String defaultValue) {
        if (value != null) {
            return value.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }
        return defaultValue;
    }

    public static String getCurrentDayReportEvent() {
        Instant date = Instant.now(Clock.system(ZoneId.of("Asia/Tashkent")));
        String format = "dd.MM.yyyy HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format)
                .withZone(ZoneId.of("Asia/Tashkent")).withLocale(Locale.getDefault());
        return String.format("%s", formatter.format(date));
    }

    public static String getSysShortYear() {
        LocalDate localDate = LocalDate.now();
        return String.valueOf(localDate.getYear()).substring(2);
    }

    public static String getRegDate(LocalDate date, String lang) {
        String regDateStr = "-";
        if (date == null) {
            return regDateStr;
        }

        int day = date.getDayOfMonth();
        String month = getMonthNameByNumberUzLatn(date.getMonthValue());
        String yearName = "y";
        if (lang != null && lang.equals("ru")) {
            month = getMonthNameByNumberCyrl(date.getMonthValue());
            yearName = "й";
        }
        int year = date.getYear();
        regDateStr = String.format("«%d» %s %d %s", day, month, year, yearName);

        return regDateStr;
    }

    public static String getMonthNameByNumberCyrl(int number) {
        int monthIndex = (number < 1 || number > 12) ? 0 : number - 1;
        String[] monthNames = {
                "январь", "февраль", "март", "апрель", "май",
                "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"};
        return monthNames[monthIndex];
    }

    public static String getMonthNameByNumberUzLatn(int number) {
        int monthIndex = (number < 1 || number > 12) ? 0 : number - 1;
        String[] monthNames = {
                "yanvar", "fevral", "mart", "aprel", "may",
                "iyun", "iyul", "avgust", "sentyabr", "oktyabr", "noyabr", "dekabr"};
        return monthNames[monthIndex];
    }

    public static Integer getMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new Date());
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static Integer getDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
