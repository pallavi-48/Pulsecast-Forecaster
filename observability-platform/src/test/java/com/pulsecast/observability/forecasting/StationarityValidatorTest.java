package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.StationarityReport;
import com.pulsecast.observability.forecasting.analysis.StationarityValidator;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.preprocessing.DifferencingTransformer;

import org.junit.jupiter.api.Test;

public class StationarityValidatorTest {

    @Test
    void validateDatasetStationarity() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadTwoMonthDataset();

        TrafficSeriesExtractor extractor =
                new TrafficSeriesExtractor();

        double[] traffic =
                extractor.extract(series);

        DifferencingTransformer transformer =
                new DifferencingTransformer();

        double[] differenced =
                transformer.difference(
                        traffic
                );

        StationarityValidator validator =
                new StationarityValidator();

        StationarityReport report =
                validator.validate(
                        differenced
                );

        System.out.println(
                "\n===== STATIONARITY REPORT =====\n"
        );

        System.out.println(
                "Overall Mean = "
                        + report.getOverallMean()
        );

        System.out.println(
                "First Half Mean = "
                        + report.getFirstHalfMean()
        );

        System.out.println(
                "Second Half Mean = "
                        + report.getSecondHalfMean()
        );

        System.out.println(
                "Overall Variance = "
                        + report.getOverallVariance()
        );

        System.out.println(
                "First Half Variance = "
                        + report.getFirstHalfVariance()
        );

        System.out.println(
                "Second Half Variance = "
                        + report.getSecondHalfVariance()
        );

        System.out.println(
                "\nStationary = "
                        + report.isStationary()
        );
    }
}