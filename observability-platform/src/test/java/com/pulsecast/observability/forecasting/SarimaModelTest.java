package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.SarimaModel;
import com.pulsecast.observability.forecasting.model.SarimaParameters;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;


import org.junit.jupiter.api.Test;

public class SarimaModelTest {

    @Test
    void trainModel() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadTwoMonthDataset();

        TrafficSeriesExtractor extractor =
                new TrafficSeriesExtractor();

        double[] traffic =
                extractor.extract(series);

        SarimaParameterSelector selector =
                new SarimaParameterSelector();

        SarimaParameters params =
                selector.selectParameters();

        SarimaModelTrainer trainer =
                new SarimaModelTrainer();

        SarimaModel model =
                trainer.train(
                        traffic,
                        params
                );

        System.out.println(
                "\n===== MODEL TRAINED =====\n"
        );

        System.out.println(
                "Mean Traffic = "
                        + model.getMeanTraffic()
        );

        System.out.println(
                "Season Length = "
                        + model.getParameters()
                        .getSeasonLength()
        );
        System.out.println(
                "AR Coefficient = "
                        + model.getArCoefficient()
        );

        System.out.println(
                "Seasonal Coefficient = "
                        + model.getSeasonalCoefficient()
        );

        System.out.println(
                "Residual Std Dev = "
                        + model.getResidualStdDev()
        );
        System.out.println("MA Coefficient = " + model.getMaCoefficient());
        System.out.println("Seasonal MA Coefficient = " + model.getSeasonalMaCoefficient());
        System.out.println("Optimized AR Coefficient = " + model.getArCoefficient());
        System.out.println("Optimized MA Coefficient = " + model.getMaCoefficient());
        System.out.println("Optimized Seasonal Coefficient = " + model.getSeasonalCoefficient());
        System.out.println("Optimized Seasonal MA Coefficient = " + model.getSeasonalMaCoefficient());
    }
}