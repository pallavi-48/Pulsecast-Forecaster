package com.pulsecast.observability.forecasting.loader;

import com.pulsecast.observability.forecasting.model.AnalysisCsvRow;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class AnalysisCsvLoader {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<AnalysisCsvRow> load(String path) {

        try {
            List<AnalysisCsvRow> rows = new ArrayList<>();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    new ClassPathResource(path).getInputStream()
                            )
                    );

            String headerLine = reader.readLine();

            if (headerLine == null) {
                throw new RuntimeException("Empty CSV file: " + path);
            }

            String[] headers = headerLine.split(",");

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split(",");

                LocalDateTime timestamp =
                        parseTimestamp(parts[0].trim());

                Map<String, Double> values = new HashMap<>();

                for (int i = 1; i < headers.length && i < parts.length; i++) {
                    String columnName = headers[i].trim();
                    String value = parts[i].trim();

                    values.put(
                            columnName,
                            value.isBlank() ? 0.0 : Double.parseDouble(value)
                    );
                }

                rows.add(
                        new AnalysisCsvRow(
                                timestamp,
                                values
                        )
                );
            }

            return rows;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load CSV: " + path,
                    e
            );
        }
    }

    private LocalDateTime parseTimestamp(String value) {
        try {
            return LocalDateTime.parse(value, FORMATTER);
        } catch (Exception e) {
            return LocalDateTime.parse(value);
        }
    }
}