package com.pulsecast.observability.forecasting.service;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.forecasting.SarimaNextDayForecaster;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.*;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ForecastCurveReplayService {

    private final ForecastCurveMetricsPublisher publisher;

    private double[] trafficActual;
    private double[] trafficForecast;
    private double[] trafficLower;
    private double[] trafficUpper;

    private double[] latencyActual;
    private double[] latencyForecast;

    private double[] errorsActual;
    private double[] errorsForecast;

    private double[] saturationActual;
    private double[] saturationForecast;

    private int index = 0;

    public ForecastCurveReplayService(
            ForecastCurveMetricsPublisher publisher
    ) {
        this.publisher = publisher;
        initialize();
    }

    private void initialize() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries historicalSeries =
                loader.loadTwoMonthDataset();

        TimeSeries currentSeries =
                loader.loadCurrent();

        SarimaParameterSelector selector =
                new SarimaParameterSelector();

        SarimaParameters parameters =
                selector.selectParameters();

        prepareMetric(
                historicalSeries,
                currentSeries,
                parameters,
                MetricType.TRAFFIC
        );

        prepareMetric(
                historicalSeries,
                currentSeries,
                parameters,
                MetricType.LATENCY
        );

        prepareMetric(
                historicalSeries,
                currentSeries,
                parameters,
                MetricType.ERRORS
        );

        prepareMetric(
                historicalSeries,
                currentSeries,
                parameters,
                MetricType.SATURATION
        );
    }

    private void prepareMetric(
            TimeSeries historicalSeries,
            TimeSeries currentSeries,
            SarimaParameters parameters,
            MetricType metricType
    ) {

        MetricSeriesExtractor extractor =
                new MetricSeriesExtractor();

        double[] historical =
                extractor.extract(
                        historicalSeries,
                        metricType
                );

        double[] actual =
                extractor.extract(
                        currentSeries,
                        metricType
                );

        SarimaModelTrainer trainer =
                new SarimaModelTrainer();

        SarimaModel model =
                trainer.train(
                        historical,
                        parameters
                );

        SarimaNextDayForecaster forecaster =
                new SarimaNextDayForecaster();

        ForecastResult result =
                forecaster.forecastNextDay(
                        model,
                        historical
                );

        if (metricType == MetricType.TRAFFIC) {

            trafficActual =
                    actual;

            trafficForecast =
                    result.getForecastValues();

            trafficLower =
                    result.getLowerBounds();

            trafficUpper =
                    result.getUpperBounds();

        } else if (metricType == MetricType.LATENCY) {

            latencyActual =
                    actual;

            latencyForecast =
                    result.getForecastValues();

        } else if (metricType == MetricType.ERRORS) {

            errorsActual =
                    actual;

            errorsForecast =
                    result.getForecastValues();

        } else if (metricType == MetricType.SATURATION) {

            saturationActual =
                    actual;

            saturationForecast =
                    result.getForecastValues();
        }
    }

    @Scheduled(fixedDelay = 2000)
    public void replay() {

        if (trafficActual == null || trafficForecast == null) {
            return;
        }

        publisher.updateTraffic(
                trafficActual[index],
                trafficForecast[index],
                trafficLower[index],
                trafficUpper[index]
        );

        publisher.updateLatency(
                latencyActual[index],
                latencyForecast[index]
        );

        publisher.updateErrors(
                errorsActual[index],
                errorsForecast[index]
        );

        publisher.updateSaturation(
                saturationActual[index],
                saturationForecast[index]
        );

        index =
                (index + 1) % trafficActual.length;
    }
}