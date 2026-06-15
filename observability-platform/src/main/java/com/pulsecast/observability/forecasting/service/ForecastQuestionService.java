package com.pulsecast.observability.forecasting.service;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.*;
import com.pulsecast.observability.forecasting.question.ForecastQuestionType;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;
import org.springframework.stereotype.Service;

@Service
public class ForecastQuestionService {

    private final WeeklyForecastService weeklyForecastService;
    private final DayForecastExtractor dayForecastExtractor;

    public ForecastQuestionService(
            WeeklyForecastService weeklyForecastService,
            DayForecastExtractor dayForecastExtractor
    ) {
        this.weeklyForecastService = weeklyForecastService;
        this.dayForecastExtractor = dayForecastExtractor;
    }

    public ForecastQuestionResponse answer(
            String metric,
            String horizon,
            String day,
            ForecastQuestionType questionType
    ) {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries historical =
                loader.loadTwoMonthDataset();

        MetricSeriesExtractor extractor =
                new MetricSeriesExtractor();

        MetricType metricType =
                MetricType.valueOf(
                        metric.toUpperCase()
                );

        double[] historicalValues =
                extractor.extract(
                        historical,
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

        double[] forecastValues;
        String questionScope;

        if (day != null && !day.isBlank()) {

            double[] weeklyForecast =
                    weeklyForecastService
                            .forecastNextWeek(
                                    model,
                                    historicalValues
                            )
                            .getForecastValues();

            forecastValues =
                    dayForecastExtractor.extractDay(
                            weeklyForecast,
                            day,
                            parameters.getSeasonLength()
                    );

            questionScope =
                    day;

        } else if ("week".equalsIgnoreCase(horizon)) {

            forecastValues =
                    weeklyForecastService
                            .forecastNextWeek(
                                    model,
                                    historicalValues
                            )
                            .getForecastValues();

            questionScope =
                    "week";

        } else {

            double[] weeklyForecast =
                    weeklyForecastService
                            .forecastNextWeek(
                                    model,
                                    historicalValues
                            )
                            .getForecastValues();

            forecastValues =
                    dayForecastExtractor.extractDay(
                            weeklyForecast,
                            "monday",
                            parameters.getSeasonLength()
                    );

            questionScope =
                    "next day";
        }

        ForecastQuestionResponse response =
                new ForecastQuestionResponse();

        response.metric =
                metricType.name();

        response.questionType =
                questionType.name();

        response.horizon =
                horizon;

        response.day =
                day;

        response.expectedAverage =
                ForecastSummaryUtil.average(
                        forecastValues
                );

        response.expectedMin =
                ForecastSummaryUtil.min(
                        forecastValues
                );

        response.expectedMax =
                ForecastSummaryUtil.max(
                        forecastValues
                );

        response.mae = null;
        response.rmse = null;
        response.complianceScore = null;
        response.compliant = null;
        response.anomalyCount = null;

        response.answer =
                buildAnswer(
                        metricType,
                        questionScope,
                        questionType,
                        response
                );

        return response;
    }

    private String buildAnswer(
            MetricType metricType,
            String scope,
            ForecastQuestionType questionType,
            ForecastQuestionResponse response
    ) {

        String metricName =
                metricType.name().toLowerCase();

        if (questionType == ForecastQuestionType.PEAK) {
            return "For "
                    + metricName
                    + " during "
                    + scope
                    + ", the highest expected value is approximately "
                    + String.format("%.2f", response.expectedMax)
                    + ".";
        }

        if (questionType == ForecastQuestionType.MINIMUM) {
            return "For "
                    + metricName
                    + " during "
                    + scope
                    + ", the lowest expected value is approximately "
                    + String.format("%.2f", response.expectedMin)
                    + ".";
        }

        if (questionType == ForecastQuestionType.TREND) {
            return buildTrendAnswer(
                    metricName,
                    scope,
                    response
            );
        }

        if (questionType == ForecastQuestionType.ANOMALY) {
            return "For "
                    + metricName
                    + " during "
                    + scope
                    + ", anomaly information can be confirmed only after actual values are observed. The forecasted range is "
                    + String.format("%.2f", response.expectedMin)
                    + " to "
                    + String.format("%.2f", response.expectedMax)
                    + ".";
        }

        if (questionType == ForecastQuestionType.COMPLIANCE) {
            return "Seasonality compliance can be evaluated only after actual values are available. Based on forecast expectations, "
                    + metricName
                    + " is expected to remain between "
                    + String.format("%.2f", response.expectedMin)
                    + " and "
                    + String.format("%.2f", response.expectedMax)
                    + ".";
        }

        return "For "
                + metricName
                + " during "
                + scope
                + ", the model expects an average of "
                + String.format("%.2f", response.expectedAverage)
                + ", with values ranging from "
                + String.format("%.2f", response.expectedMin)
                + " to "
                + String.format("%.2f", response.expectedMax)
                + ".";
    }

    private String buildTrendAnswer(
            String metricName,
            String scope,
            ForecastQuestionResponse response
    ) {

        double spread =
                response.expectedMax - response.expectedMin;

        double relativeSpread =
                spread / Math.max(
                        response.expectedAverage,
                        1.0
                );

        if (relativeSpread > 0.75) {
            return metricName
                    + " is expected to vary significantly during "
                    + scope
                    + ". Forecast average is "
                    + String.format("%.2f", response.expectedAverage)
                    + ", but it may range from "
                    + String.format("%.2f", response.expectedMin)
                    + " to "
                    + String.format("%.2f", response.expectedMax)
                    + ".";
        }

        return metricName
                + " is expected to remain relatively stable during "
                + scope
                + ". Forecast average is approximately "
                + String.format("%.2f", response.expectedAverage)
                + ".";
    }
}