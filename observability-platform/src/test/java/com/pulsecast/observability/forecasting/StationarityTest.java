package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.StationarityAnalyzer;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.preprocessing.DifferencingTransformer;
import org.junit.jupiter.api.Test;

public class StationarityTest {

    @Test
    void analyzeTrafficStationarity() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadTwoMonthDataset();

        double[] traffic =
                series.getTrafficSeries();

        DifferencingTransformer transformer =
                new DifferencingTransformer();

        double[] differenced =
                transformer.difference(
                        traffic
                );

        StationarityAnalyzer analyzer =
                new StationarityAnalyzer();

        analyzer.analyze(
                traffic,
                differenced
        );
    }
}