package com.icandoit.salary;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CsvInputTest {

    public static void main(String[] args) throws IOException {
        URL inputFile = CsvInputTest.class.getClassLoader().getResource("HourList201403.csv");
        List<SalaryRecord> salaryRecords = CsvReader.readCsv(new File(inputFile.getFile()));
        CsvReader.calculateMonthlyWages(salaryRecords);
    }
}
