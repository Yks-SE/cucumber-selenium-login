
package com.example.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvData {
    private static final Map<String, List<Map<String, String>>> CACHE = new HashMap<>();

    public static void cache(String path) {
        read(path); // populates cache
    }

    public static List<Map<String, String>> read(String path) {
        if (CACHE.containsKey(path)) return CACHE.get(path);
        try {
            InputStream is = CsvData.class.getClassLoader().getResourceAsStream(path);
            if (is == null) throw new IllegalArgumentException("CSV not found on classpath: " + path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader);
            List<Map<String, String>> rows = new ArrayList<>();
            for (CSVRecord r : parser) {
                Map<String, String> m = new HashMap<>();
                for (String h : parser.getHeaderNames()) {
                    m.put(h, r.get(h));
                }
                rows.add(m);
            }
            CACHE.put(path, rows);
            return rows;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
