package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.DatasetAnalysisService;
import com.pulsecast.observability.forecasting.analysis.MetricStatistics;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatasetAnalysisTest {

    @Test
    void shouldAnalyzeTraffic() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadHistorical();

        DatasetAnalysisService analysis =
                new DatasetAnalysisService();

        MetricStatistics stats =
                analysis.analyzeTraffic(series);

        assertTrue(stats.mean() > 0);
        assertTrue(stats.max() >= stats.mean());
        assertTrue(stats.min() <= stats.mean());
    }

    @Test
    void shouldAnalyzeCpu() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadHistorical();

        DatasetAnalysisService analysis =
                new DatasetAnalysisService();

        MetricStatistics stats =
                analysis.analyzeCpu(series);

        assertTrue(stats.mean() > 0);
    }
}