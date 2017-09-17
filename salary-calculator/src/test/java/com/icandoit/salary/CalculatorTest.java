package com.icandoit.salary;

import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {

    @Test
    public void calculatesSalary() throws Exception {
        Instant start = Instant.parse("2017-09-15T08:00:00Z");
        Instant end = Instant.parse("2017-09-15T20:00:00Z");
        double salary = Calculator.calculateSalary(start, end);

        assertEquals(30 + 9.8 + 20.625, salary, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenEndDateBeforeStart() throws Exception {
        Instant start = Instant.parse("2017-09-15T08:00:00Z");
        Instant end = Instant.parse("2017-09-15T07:00:00Z");
        Calculator.calculateSalary(start, end);
    }

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
}