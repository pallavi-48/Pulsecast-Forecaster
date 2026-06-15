package com.pulsecast.observability.forecasting.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DayForecastExtractor {

    public double[] extractDay(
            double[] weeklyForecast,
            String day,
            int pointsPerDay
    ) {

        int dayIndex =
                switch (day.toLowerCase()) {
                    case "monday" -> 0;
                    case "tuesday" -> 1;
                    case "wednesday" -> 2;
                    case "thursday" -> 3;
                    case "friday" -> 4;
                    case "saturday" -> 5;
                    case "sunday" -> 6;
                    default -> 0;
                };

        int start =
                dayIndex * pointsPerDay;

        int end =
                start + pointsPerDay;

        return Arrays.copyOfRange(
                weeklyForecast,
                start,
                end
        );
    }
}