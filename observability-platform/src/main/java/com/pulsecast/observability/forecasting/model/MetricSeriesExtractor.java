package com.pulsecast.observability.forecasting.model;

import com.pulsecast.observability.forecasting.domain.TimePoint;
import com.pulsecast.observability.forecasting.domain.TimeSeries;

import java.util.List;

public class MetricSeriesExtractor {

    public double[] extract(
            TimeSeries series,
            MetricType metricType
    ) {

        List<TimePoint> points =
                series.getPoints();

        double[] values =
                new double[points.size()];

        for (int i = 0; i < points.size(); i++) {

            TimePoint point =
                    points.get(i);

            values[i] =
                    switch (metricType) {

                        case TRAFFIC ->
                                point.getRequestsPerSecond();

                        case LATENCY ->
                                point.getResponseTimeMs();

                        case ERRORS ->
                                point.getErrorRatePercent();

                        case SATURATION ->
                                point.getCpuUsagePercent();
                    };
        }

        return values;
    }
}