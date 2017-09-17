package com.icandoit.salary;

import java.time.LocalDate;

public class SalaryRecord {

    private String name;
    private int id;
    private LocalDate date;
    private double salary;


    public SalaryRecord(String name, int id, LocalDate date, double salary) {
        this.name = name;
        this.id = id;
        this.date = date;
        this.salary = salary;
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

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "SalaryRecord{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", date=" + date +
                ", salary=" + salary +
                '}';
    }
}
