package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatasetLoaderTest {

    @Test
    void shouldLoadHistoricalDataset() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadHistorical();

        assertNotNull(series);

        assertTrue(
                series.size() > 0
        );
    }

    @Test
    void shouldLoadCurrentDataset() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadCurrent();

        assertNotNull(series);

        assertTrue(
                series.size() > 0
        );
    }

    @Test
    void shouldExtractTrafficSeries() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadHistorical();

        double[] traffic =
                series.getTrafficSeries();

        assertEquals(
                series.size(),
                traffic.length
        );
    }
}