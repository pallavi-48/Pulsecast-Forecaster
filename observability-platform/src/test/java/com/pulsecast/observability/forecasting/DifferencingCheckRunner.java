package com.pulsecast.observability.forecasting.visualization;

import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.preprocessing.DifferencingTransformer;

public class DifferencingCheckRunner {

    public static void main(String[] args) {

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

        double[] diff =
                transformer.difference(traffic);

        System.out.println("Original");

        for(int i = 0; i < 10; i++) {

            System.out.println(
                    traffic[i]
            );
        }

        System.out.println();

        System.out.println("Differenced");

        for(int i = 0; i < 10; i++) {

            System.out.println(
                    diff[i]
            );
        }
    }
}