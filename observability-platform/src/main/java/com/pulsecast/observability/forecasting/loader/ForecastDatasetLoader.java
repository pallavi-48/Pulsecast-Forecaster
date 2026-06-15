package com.pulsecast.observability.forecasting.loader;

import com.pulsecast.observability.forecasting.domain.TimePoint;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ForecastDatasetLoader {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm:ss"
            );

    public TimeSeries loadHistorical() {
        return load("dataset/2_month_metrics.csv");
    }

    public TimeSeries loadCurrent() {
        return load("dataset/current_metrics.csv");
    }

    public TimeSeries loadTwoMonthDataset() {
        return load("dataset/2_month_metrics.csv");
    }

    public TimeSeries loadCurrentMetricsDataset() {
        return load("dataset/current_metrics.csv");
    }

    private TimeSeries load(String path) {

        try {

            List<TimePoint> points =
                    new ArrayList<>();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    new ClassPathResource(path)
                                            .getInputStream()
                            )
                    );

            String line;

            reader.readLine(); // skip header

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] row =
                        line.split(",");

                if (row.length < 11) {
                    continue;
                }

                TimePoint point =
                        new TimePoint(
                                LocalDateTime.parse(row[0].trim(), FORMATTER),
                                Double.parseDouble(row[1].trim()),
                                Double.parseDouble(row[2].trim()),
                                Double.parseDouble(row[3].trim()),
                                Double.parseDouble(row[4].trim()),
                                Double.parseDouble(row[5].trim()),
                                Double.parseDouble(row[6].trim()),
                                Double.parseDouble(row[7].trim()),
                                Double.parseDouble(row[8].trim()),
                                Double.parseDouble(row[9].trim()),
                                Double.parseDouble(row[10].trim())
                        );

                points.add(point);
            }

            return new TimeSeries(points);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to load dataset: " + path,
                    e
            );
        }
    }
}