package com.pulsecast.observability.forecasting.model;

import java.util.List;

public class ChartDataResponse {

    public String metric;
    public String view;

    public String series1Name;
    public String series2Name;

    public List<String> timestamps;

    public double[] series1;
    public double[] series2;

    public ChartDataResponse(
            String metric,
            String view,
            String series1Name,
            String series2Name,
            List<String> timestamps,
            double[] series1,
            double[] series2
    ) {
        this.metric = metric;
        this.view = view;
        this.series1Name = series1Name;
        this.series2Name = series2Name;
        this.timestamps = timestamps;
        this.series1 = series1;
        this.series2 = series2;
    }
}