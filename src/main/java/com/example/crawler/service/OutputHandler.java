package com.example.crawler.service;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class OutputHandler {

    public void saveToCSV(Map<String, List<String>> productUrls, String fileName) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            productUrls.forEach((domain, urls) -> {
                urls.forEach(url -> writer.writeNext(new String[]{domain, url}));
            });
        }
    }
}
