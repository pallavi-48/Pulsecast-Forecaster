package com.pulsecast.observability.forecasting.controller;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.forecasting.SarimaNextDayForecaster;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.*;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forecast/chart")
public class ForecastChartController {

    @GetMapping("/{metric}")
    public ForecastChartResponse chart(
            @PathVariable String metric
    ) {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries historicalSeries =
                loader.loadTwoMonthDataset();

        TimeSeries currentSeries =
                loader.loadCurrent();

        MetricType metricType =
                MetricType.valueOf(
                        metric.toUpperCase()
                );

        MetricSeriesExtractor extractor =
                new MetricSeriesExtractor();

        double[] historicalValues =
                extractor.extract(
                        historicalSeries,
                        metricType
                );

        double[] actualValues =
                extractor.extract(
                        currentSeries,
                        metricType
                );

        SarimaParameterSelector selector =
                new SarimaParameterSelector();

        SarimaParameters parameters =
                selector.selectParameters();

        SarimaModelTrainer trainer =
                new SarimaModelTrainer();

        SarimaModel model =
                trainer.train(
                        historicalValues,
                        parameters
                );

        SarimaNextDayForecaster forecaster =
                new SarimaNextDayForecaster();

        ForecastResult forecastResult =
                forecaster.forecastNextDay(
                        model,
                        historicalValues
                );

        return new ForecastChartResponse(
                metricType.name(),
                actualValues,
                forecastResult.getForecastValues(),
                forecastResult.getLowerBounds(),
                forecastResult.getUpperBounds()
        );
    }
}