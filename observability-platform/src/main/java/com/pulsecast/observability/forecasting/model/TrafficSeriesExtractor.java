package com.pulsecast.observability.forecasting.model;

import com.pulsecast.observability.forecasting.domain.TimeSeries;

public class TrafficSeriesExtractor {

    public double[] extract(
            TimeSeries series
    ) {

        return series.getTrafficSeries();
    }
}