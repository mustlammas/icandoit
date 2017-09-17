package com.icandoit.salary;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.temporal.ChronoField;
import java.util.List;

/**
 * Calculates daily salary based on the hours worked. The total salary consists of three parts: normal hourly wage,
 * evening wage and overtime pay.
 * <p>
 * Normal working hours are 06:00 - 18:00, 8 hours per day.
 * Evening hours are 18:00 - 06:00.
 * Overtime pay has three levels:
 * 1. First two hours: 1.25 x normal wage
 * 1. Next two hours: 1.5 x normal wage
 * 1. After four hours: 2 x normal wage
 */
public class Calculator {

    private static final double NORMAL_WORKING_HOURS = 8;
    private static final double HOURLY_WAGE = 3.75;
    private static final double EVENING_WAGE = HOURLY_WAGE + 1.15;
    private static final int EVENING_TIME_START_MINUTE = 18 * 60;
    private static final int EVENING_TIME_END_MINUTE = 6 * 60;
    private static final int MINUTES_IN_DAY = 24 * 60;

    public static double calculateSalary(List<TimeRecord> shifts) {
        double eveningHours = getEveningHours(shifts);
        double totalHours = getHours(shifts);
        double normalHours = totalHours - eveningHours;
        double normalSalary = calculateSalaryForNormalHours(normalHours);
        double eveningHoursSalary = calculateSalaryForEveningHours(eveningHours);
        double overtimeSalary = calculateOvertimeSalary(totalHours);
        double totalSalary = normalSalary + eveningHoursSalary + overtimeSalary;
        return round(totalSalary);
    }

    private static double round(double value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    private static double getHours(List<TimeRecord> shifts) {
        double hours = 0;
        for (TimeRecord shift : shifts) {
            hours += getHours(shift.getStart(), shift.getEnd());
        }

        return hours;
    }

    protected static double getHours(Instant start, Instant end) {
        return Duration.between(start, end).toMinutes() / 60.0;
    }

    private static double getEveningHours(List<TimeRecord> shifts) {
        double hours = 0;
        for (TimeRecord shift : shifts) {
            hours += getEveningHours(shift.getStart(), shift.getEnd());
        }

        return hours;
    }

    private static double calculateSalaryForNormalHours(double hours) {
        double normalHours = Math.min(NORMAL_WORKING_HOURS, hours);
        return normalHours * HOURLY_WAGE;
    }

    private static double calculateSalaryForEveningHours(double eveningHours) {
        return eveningHours * EVENING_WAGE;
    }

    /**
     * First 2 Hours > 8 Hours = Hourly Wage + 25%
     * Next 2 Hours = Hourly Wage + 50%
     * After That = Hourly Wage + 100%
     */
    protected static double calculateOvertimeSalary(double totalHours) {
        double overtime = totalHours - NORMAL_WORKING_HOURS;
        if (overtime > 0) {
            double firstTwoHours = Math.min(2, overtime) * HOURLY_WAGE * 1.25;
            double nextTwoHours = Math.min(2, Math.max(0, overtime - 2)) * HOURLY_WAGE * 1.5;
            double afterThat = Math.max(0, overtime - 4) * HOURLY_WAGE * 2;
            return firstTwoHours + nextTwoHours + afterThat;
        } else {
            return 0;
        }
    }

    /**
     * Calculates hours worked during evening time (18:00 - 06:00).
     */
    protected static double getEveningHours(Instant start, Instant end) {
        LocalDateTime startDateTime = LocalDateTime.ofInstant(start, ZoneId.of("UTC"));
        LocalDateTime endDateTime = LocalDateTime.ofInstant(end, ZoneId.of("UTC"));
        int startMinute = getEveningHoursStartMinute(startDateTime);
        int endMinute = getEveningHoursEndMinute(startDateTime, endDateTime);

        return Math.max(0, endMinute - startMinute) / 60.0;
    }

    private static int getEveningHoursStartMinute(LocalDateTime startDateTime) {
        int startMinute = startDateTime.get(ChronoField.MINUTE_OF_DAY);

        // If work starts before 6 AM
        if (startMinute < (6 * 60)) {
            return startMinute;
        } else {
            return Math.max(startMinute, EVENING_TIME_START_MINUTE);
        }
    }

    private static int getEveningHoursEndMinute(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        int startMinute = startDateTime.get(ChronoField.MINUTE_OF_DAY);
        int endMinute = endDateTime.get(ChronoField.MINUTE_OF_DAY);
        int eveningTimeEndMinute = EVENING_TIME_END_MINUTE + MINUTES_IN_DAY;

        // If work ends on the next day
        if (endMinute < startMinute) {
            endMinute = endMinute + MINUTES_IN_DAY;
        }

        return Math.min(endMinute, eveningTimeEndMinute);
    }
}
