package com.icandoit.salary;

import java.time.Instant;
import java.time.LocalDate;

public class TimeRecord {

    private String name;
    private int id;
    private LocalDate date;
    private Instant start;
    private Instant end;

    public TimeRecord(String name, int id, LocalDate date, Instant start, Instant end) {
        validateName(name);
        validateNotNull(date, "'date' parameter can not be null");
        validateNotNull(start, "'start' parameter can not be null");
        validateNotNull(end, "'end' parameter can not be null");

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be earlier than end date. " +
                    "Start date: '" + start + "', end date: '" + end + "'");
        }

        this.name = name;
        this.id = id;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    private void validateNotNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("'name' parameter can not be null");
        }
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }
}
