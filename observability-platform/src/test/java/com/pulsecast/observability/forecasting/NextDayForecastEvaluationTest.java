//package com.pulsecast.observability.forecasting;
//
//import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
//import com.pulsecast.observability.forecasting.domain.TimeSeries;
//import com.pulsecast.observability.forecasting.evaluation.AccuracyReport;
//import com.pulsecast.observability.forecasting.evaluation.AnomalyDetector;
//import com.pulsecast.observability.forecasting.evaluation.AnomalyPoint;
//import com.pulsecast.observability.forecasting.evaluation.ForecastAccuracyEvaluator;
//import com.pulsecast.observability.forecasting.forecasting.SarimaNextDayForecaster;
//import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
//import com.pulsecast.observability.forecasting.model.ForecastResult;
//import com.pulsecast.observability.forecasting.model.SarimaModel;
//import com.pulsecast.observability.forecasting.model.SarimaParameters;
//import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
//import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//public class NextDayForecastEvaluationTest {
//
//    @Test
//    void evaluateNextDayForecastAgainstTestDataset() {
//
//        ForecastDatasetLoader loader =
//                new ForecastDatasetLoader();
//
//        TimeSeries historicalSeries =
//                loader.loadTwoMonthDataset();
//
//        TimeSeries testSeries =
//                loader.loadCurrent();
//
//        TrafficSeriesExtractor extractor =
//                new TrafficSeriesExtractor();
//
//        double[] historicalTraffic =
//                extractor.extract(historicalSeries);
//
//        double[] actualTraffic =
//                extractor.extract(testSeries);
//
//        SarimaParameterSelector selector =
//                new SarimaParameterSelector();
//
//        SarimaParameters parameters =
//                selector.selectParameters();
//
//        SarimaModelTrainer trainer =
//                new SarimaModelTrainer();
//
//        SarimaModel model =
//                trainer.train(
//                        historicalTraffic,
//                        parameters
//                );
//
//        SarimaNextDayForecaster forecaster =
//                new SarimaNextDayForecaster();
//
//        ForecastResult forecastResult =
//                forecaster.forecastNextDay(
//                        model,
//                        historicalTraffic
//                );
//
//        double[] predictedTraffic =
//                forecastResult.getForecastValues();
//
//        ForecastAccuracyEvaluator evaluator =
//                new ForecastAccuracyEvaluator();
//
//        AccuracyReport report =
//                evaluator.evaluate(
//                        actualTraffic,
//                        predictedTraffic
//                );
//
//        double threshold =
//                2 * model.getResidualStdDev();
//
//        AnomalyDetector anomalyDetector =
//                new AnomalyDetector();
//
//        List<AnomalyPoint> anomalies =
//                anomalyDetector.detect(
//                        actualTraffic,
//                        predictedTraffic,
//                        threshold
//                );
//
//        System.out.println(
//                "\n===== NEXT DAY FORECAST EVALUATION =====\n"
//        );
//
//        System.out.println(
//                "MAE = " + report.getMae()
//        );
//
//        System.out.println(
//                "RMSE = " + report.getRmse()
//        );
//
//        System.out.println(
//                "Anomaly Threshold = " + threshold
//        );
//
//        System.out.println(
//                "Total Anomalies = " + anomalies.size()
//        );
//
//        System.out.println(
//                "\n===== SAMPLE ACTUAL VS PREDICTED =====\n"
//        );
//
//        for (int i = 0; i < Math.min(20, actualTraffic.length); i++) {
//
//            System.out.printf(
//                    "Point %d | Actual = %.2f | Predicted = %.2f%n",
//                    i + 1,
//                    actualTraffic[i],
//                    predictedTraffic[i]
//            );
//        }
//
//        System.out.println(
//                "\n===== ANOMALIES =====\n"
//        );
//
//        for (AnomalyPoint anomaly : anomalies) {
//
//            System.out.printf(
//                    "Point %d | Actual = %.2f | Predicted = %.2f | Error = %.2f%n",
//                    anomaly.getIndex(),
//                    anomaly.getActual(),
//                    anomaly.getPredicted(),
//                    anomaly.getError()
//            );
//        }
//    }
//}