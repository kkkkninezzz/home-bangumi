package top.rainine.homebangumi.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * @authoer rainine
 * @date 2024/5/19 15:08
 * @desc
 */
public class HbDateUtils {

    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private HbDateUtils() {}

    /**
     * 转换为毫秒时间戳
     * 时区默认为 Asia/Shanghai
     * */
    public static long toMills(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Asia/Shanghai"));
        return zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * 获取当前时间
     * */
    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
    }

    /**
     * 根据时间戳获取指定时间
     * */
    public static LocalDateTime now(long mills) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(mills), ZoneId.of("Asia/Shanghai"));
    }

    /**
     * 获取指定时间所在的一周的开始时间
     * */
    public static LocalDateTime getStartOfWeek(LocalDateTime localDateTime) {
        return getStartOfDay(localDateTime).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    /**
     * 获取当前时间的那一天的0点
     * */
    public static LocalDateTime getStartOfDay() {
        return getStartOfDay(now());
    }

    /**
     * 获取当前时间的那一天的0点
     * */
    public static LocalDateTime getStartOfDay(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate().atStartOfDay();
    }

    public static long getStartOfDayMills(LocalDate localDate) {
        return toMills(localDate.atStartOfDay());
    }

    /**
     * 获取季度开始的时间
     * */
    public static LocalDateTime getStartOfQuarter(LocalDateTime now) {
        // 获取当前年份
        int year = now.getYear();

        // 确定当前季度的开始月份
        Month startMonth;
        if (now.getMonthValue() <= 3) {
            startMonth = Month.JANUARY;  // 第一季度
        } else if (now.getMonthValue() <= 6) {
            startMonth = Month.APRIL;    // 第二季度
        } else if (now.getMonthValue() <= 9) {
            startMonth = Month.JULY;     // 第三季度
        } else {
            startMonth = Month.OCTOBER;  // 第四季度
        }

        // 设置当前季度的开始时间为季度第一天的00:00
        return LocalDateTime.of(year, startMonth, 1, 0, 0, 0);
    }

    /**
     * 获取上个季度开始的时间
     * */
    public static LocalDateTime getStartOfPreQuarter(LocalDateTime now) {
        // 获取当前年份
        int year = now.getYear();
        Month startMonth;

        // 确定上个季度的开始月份和年份
        switch (now.getMonth()) {
            case JANUARY:
            case FEBRUARY:
            case MARCH:
                // 上个季度是去年的第四季度
                startMonth = Month.OCTOBER;
                year -= 1;
                break;
            case APRIL:
            case MAY:
            case JUNE:
                startMonth = Month.JANUARY;
                break;
            case JULY:
            case AUGUST:
            case SEPTEMBER:
                startMonth = Month.APRIL;
                break;
            case OCTOBER:
            case NOVEMBER:
            case DECEMBER:
                startMonth = Month.JULY;
                break;
            default:
                throw new IllegalStateException(STR."Unexpected value: \{now.getMonth()}");
        }

        // 设置上个季度的开始时间为季度第一天的00:00
        return LocalDateTime.of(year, startMonth, 1, 0, 0, 0);
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DEFAULT_DATE_TIME_FORMATTER);
    }
}
