package com.icandoit.salary;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {

    @Test
    public void getsNormalWorkingHours() throws Exception {
        Instant start = Instant.parse("2017-09-15T08:00:00Z");
        Instant end = Instant.parse("2017-09-15T16:30:00Z");
        double normalHours = Calculator.getHours(start, end);

        assertEquals(8.5, normalHours, 0.001);
    }

    @Test
    public void getsNormalWorkingHours2() throws Exception {
        Instant start = Instant.parse("2017-09-15T08:00:00Z");
        Instant end = Instant.parse("2017-09-15T09:15:00Z");
        double normalHours = Calculator.getHours(start, end);

        assertEquals(1.25, normalHours, 0.001);
    }

    @Test
    public void getsEveningHours() {
        Instant start = Instant.parse("2017-09-15T08:00:00Z");
        Instant end = Instant.parse("2017-09-15T19:00:00Z");
        double eveningHours = Calculator.getEveningHours(start, end);

        assertEquals(1, eveningHours, 0.001);
    }

    @Test
    public void getsEveningHoursThroughMidnight() {
        Instant start = Instant.parse("2017-09-15T21:15:00Z");
        Instant end = Instant.parse("2017-09-16T02:00:00Z");
        double eveningHours = Calculator.getEveningHours(start, end);

        assertEquals(4.75, eveningHours, 0.001);
    }

    @Test
    public void getsEveningHoursBeforeNormalHoursStart() {
        Instant start = Instant.parse("2017-09-15T01:00:00Z");
        Instant end = Instant.parse("2017-09-15T03:45:00Z");
        double eveningHours = Calculator.getEveningHours(start, end);

        assertEquals(2.75, eveningHours, 0.001);
    }

    @Test
    public void calculatesOvertimeSalaryForFirstHour() {
        double overtimeSalary = Calculator.calculateOvertimeSalary(9);

        assertEquals(4.6875, overtimeSalary, 0.01);
    }

    @Test
    public void calculatesOvertimeSalaryForThreeHours() {
        double overtimeSalary = Calculator.calculateOvertimeSalary(11);

        assertEquals((4.6875 * 2) + 5.625, overtimeSalary, 0.01);
    }

    @Test
    public void calculatesOvertimeSalaryForSevenHours() {
        double overtimeSalary = Calculator.calculateOvertimeSalary(15);

        assertEquals((4.6875 * 2) + (5.625 * 2) + (3 * 7.5), overtimeSalary, 0.01);
    }

    @Test
    public void calculatesSalaryForMultipleShifts() {
        List<TimeRecord> shifts = Arrays.asList(
                new TimeRecord("Janet Java", 1, LocalDate.of(2017, 9, 17), Instant.parse("2017-09-17T08:00:00Z"), Instant.parse("2017-09-17T10:00:00Z")),
                new TimeRecord("Janet Java", 1, LocalDate.of(2017, 9, 17), Instant.parse("2017-09-17T11:00:00Z"), Instant.parse("2017-09-17T12:00:00Z")),
                new TimeRecord("Janet Java", 1, LocalDate.of(2017, 9, 17), Instant.parse("2017-09-17T14:00:00Z"), Instant.parse("2017-09-17T16:00:00Z"))
        );

        double salary = Calculator.calculateSalary(shifts);

        double expectedNormalSalary = 3.75 * 5;
        assertEquals(expectedNormalSalary, salary, 0.001);
    }

    @Test
    public void calculatesSalaryForMultipleShiftsWithEveningHours() {
        List<TimeRecord> shifts = Arrays.asList(
                new TimeRecord("Janet Java", 1, LocalDate.of(2017, 9, 17), Instant.parse("2017-09-17T08:00:00Z"), Instant.parse("2017-09-17T10:00:00Z")),
                new TimeRecord("Janet Java", 1, LocalDate.of(2017, 9, 17), Instant.parse("2017-09-17T11:00:00Z"), Instant.parse("2017-09-17T12:00:00Z")),
                new TimeRecord("Janet Java", 1, LocalDate.of(2017, 9, 17), Instant.parse("2017-09-17T17:00:00Z"), Instant.parse("2017-09-17T22:00:00Z"))
        );

        double salary = Calculator.calculateSalary(shifts);

        double expectedNormalSalary = 3.75 * 4;
        double expectedEveningSalary = 4 * (3.75 + 1.15);
        assertEquals(expectedNormalSalary + expectedEveningSalary, salary, 0.001);
    }

    @Test
    public void calculatesSalaryForMultipleShiftsWithEveningHoursAndOvertime() {
        List<TimeRecord> shifts = Arrays.asList(
                new TimeRecord("Janet Java", 1, LocalDate.of(2017, 9, 17), Instant.parse("2017-09-17T08:00:00Z"), Instant.parse("2017-09-17T10:00:00Z")),
                new TimeRecord("Janet Java", 1, LocalDate.of(2017, 9, 17), Instant.parse("2017-09-17T11:00:00Z"), Instant.parse("2017-09-17T12:00:00Z")),
                new TimeRecord("Janet Java", 1, LocalDate.of(2017, 9, 17), Instant.parse("2017-09-17T17:00:00Z"), Instant.parse("2017-09-17T22:00:00Z")),
                new TimeRecord("Janet Java", 1, LocalDate.of(2017, 9, 17), Instant.parse("2017-09-17T23:00:00Z"), Instant.parse("2017-09-18T05:00:00Z"))
        );

        double salary = Calculator.calculateSalary(shifts);

        double expectedNormalSalary = 3.75 * 4;
        double expectedEveningSalary = 10 * (3.75 + 1.15);
        double expectedOvertimeSalary = (2 * 3.75 * 1.25) + (2 * 3.75 * 1.5) + (2 * 3.75 * 2);
        assertEquals(expectedNormalSalary + expectedEveningSalary + expectedOvertimeSalary, salary, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenEndDateBeforeStart() throws Exception {
        List<TimeRecord> shifts = Arrays.asList(
                new TimeRecord("Janet Java", 1, LocalDate.of(2017, 9, 17), Instant.parse("2017-09-17T08:00:00Z"), Instant.parse("2017-09-17T07:00:00Z"))
        );

        Calculator.calculateSalary(shifts);
    }
}