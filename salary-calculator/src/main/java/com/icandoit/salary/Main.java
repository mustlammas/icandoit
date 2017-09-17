package com.icandoit.salary;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {

        InputStream input = Main.class.getClassLoader().getResourceAsStream("HourList201403.csv");

        Reader in = new InputStreamReader(input);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
        for (CSVRecord record : records) {
            // Person Name,Person ID,Date,Start,End
            String name = record.get("Person Name");
            String id = record.get("Person ID");
            String date = record.get("Date");
            String start = record.get("Start");
            String end = record.get("End");
            System.out.println(name + " : " + id + " : " + date + " : " + start + " : " + end);
        }
    }
}
