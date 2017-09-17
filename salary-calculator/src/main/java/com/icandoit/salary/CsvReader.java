package com.icandoit.salary;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CsvReader {

    public static List<SalaryRecord> readCsv(File file) throws IOException {
        Reader in = new FileReader(file);
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);

        Set<TimeRecord> result = new HashSet<>();
        for (CSVRecord record : records) {
            String name = record.get("Person Name");
            int id = Integer.parseInt(record.get("Person ID"));
            String date = record.get("Date");
            String start = record.get("Start");
            String end = record.get("End");

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yyyy H:m VV");
            String startStr = date + " " + start + " Z";
            String endStr = date + " " + end + " Z";

            LocalDateTime s = LocalDateTime.parse(startStr, dateTimeFormatter);
            LocalDateTime e = LocalDateTime.parse(endStr, dateTimeFormatter);
            if (s.isAfter(e)) {
                e = e.plusDays(1);
            }

            TimeRecord timeRecord = new TimeRecord(
                    name,
                    id,
                    s.toLocalDate(),
                    s.toInstant(ZoneOffset.UTC),
                    e.toInstant(ZoneOffset.UTC));
            result.add(timeRecord);
        }

        return createSalaryRecords(result);
    }

    private static List<SalaryRecord> createSalaryRecords(Set<TimeRecord> timeRecords) {
        Map<Integer, Map<LocalDate, List<TimeRecord>>> records = new HashMap<>();

        for (TimeRecord timeRecord : timeRecords) {
            int id = timeRecord.getId();
            LocalDate date = timeRecord.getDate();
            if (records.containsKey(id)) {
                Map<LocalDate, List<TimeRecord>> dateRecords = records.get(id);
                if (dateRecords.containsKey(date)) {
                    List<TimeRecord> timeRecords1 = dateRecords.get(date);
                    timeRecords1.add(timeRecord);
                } else {
                    List<TimeRecord> timeRecordList = new ArrayList<>();
                    timeRecordList.add(timeRecord);
                    dateRecords.put(date, timeRecordList);
                }
            } else {
                List<TimeRecord> timeRecordList = new ArrayList<>();
                timeRecordList.add(timeRecord);
                Map<LocalDate, List<TimeRecord>> dateRecords = new HashMap<>();
                dateRecords.put(date, timeRecordList);
                records.put(id, dateRecords);
            }
        }

        List<SalaryRecord> salaryRecords = new ArrayList<>();
        for (Map.Entry<Integer, Map<LocalDate, List<TimeRecord>>> personEntry : records.entrySet()) {
            for (Map.Entry<LocalDate, List<TimeRecord>> dateEntry : personEntry.getValue().entrySet()) {
                String name = dateEntry.getValue().get(0).getName();
                int id = personEntry.getKey();
                double salary = Calculator.calculateSalary(dateEntry.getValue());
                salaryRecords.add(new SalaryRecord(name, id, dateEntry.getKey(), salary));
            }
        }

        return salaryRecords;
    }

    public static void calculateMonthlyWages(List<SalaryRecord> salaryRecords) {
        // TODO:
    }
}
