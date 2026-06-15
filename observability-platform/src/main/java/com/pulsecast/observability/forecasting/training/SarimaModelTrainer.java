package com.pulsecast.observability.forecasting.training;

import com.pulsecast.observability.forecasting.analysis.PacfCalculator;
import com.pulsecast.observability.forecasting.model.SarimaModel;
import com.pulsecast.observability.forecasting.model.SarimaParameters;
import com.pulsecast.observability.forecasting.preprocessing.DifferencingTransformer;
import com.pulsecast.observability.forecasting.optimization.CoefficientOptimizer;
import com.pulsecast.observability.forecasting.optimization.OptimizationResult;
public class SarimaModelTrainer {

    public SarimaModel train(double[] values, SarimaParameters parameters) {

        double mean = calculateMean(values);

        DifferencingTransformer transformer = new DifferencingTransformer();
        double[] differenced = transformer.difference(values);

        PacfCalculator pacfCalculator = new PacfCalculator();
        double[] pacf = pacfCalculator.calculate(differenced, 5);

        int seasonLength = parameters.getSeasonLength();

        CoefficientOptimizer optimizer =
                new CoefficientOptimizer();

        OptimizationResult optimizationResult =
                optimizer.optimize(
                        values,
                        mean,
                        seasonLength
                );

        double arCoefficient =
                optimizationResult.getArCoefficient();

        double maCoefficient =
                optimizationResult.getMaCoefficient();

        double seasonalCoefficient =
                optimizationResult.getSeasonalCoefficient();

        double seasonalMaCoefficient =
                optimizationResult.getSeasonalMaCoefficient();

        double[] finalResiduals =
                calculateResiduals(
                        values,
                        mean,
                        arCoefficient,
                        maCoefficient,
                        seasonalCoefficient,
                        seasonalMaCoefficient,
                        seasonLength
                );

        double residualStdDev =
                calculateStdDev(finalResiduals);

        return new SarimaModel(
                parameters,
                mean,
                arCoefficient,
                maCoefficient,
                seasonalCoefficient,
                seasonalMaCoefficient,
                residualStdDev,
                finalResiduals
        );
    }

    private double[] calculateResiduals(
            double[] values,
            double mean,
            double arCoefficient,
            double maCoefficient,
            double seasonalCoefficient,
            double seasonalMaCoefficient,
            int seasonLength
    ) {
        double[] residuals = new double[values.length];

        for (int i = seasonLength; i < values.length; i++) {

            double previousValue = values[i - 1];
            double seasonalValue = values[i - seasonLength];

            double previousResidual = residuals[i - 1];
            double seasonalResidual = residuals[i - seasonLength];

            double predicted =
                    mean
                            + arCoefficient * (previousValue - mean)
                            + seasonalCoefficient * (seasonalValue - mean)
                            + maCoefficient * previousResidual
                            + seasonalMaCoefficient * seasonalResidual;

            residuals[i] = values[i] - predicted;
        }

        return residuals;
    }

    private double calculateMean(double[] values) {
        double sum = 0.0;

        for (double value : values) {
            sum += value;
        }

        return sum / values.length;
    }

    private double calculateLagCorrelation(double[] values, int lag) {

        if (values.length <= lag) {
            return 0.0;
        }

        double mean = calculateMean(values);

        double numerator = 0.0;
        double denominator = 0.0;

        for (double value : values) {
            denominator += Math.pow(value - mean, 2);
        }

        if (Math.abs(denominator) < 1e-10) {
            return 0.0;
        }

        for (int i = lag; i < values.length; i++) {
            numerator +=
                    (values[i] - mean)
                            * (values[i - lag] - mean);
        }

        return numerator / denominator;
    }

    private double calculateStdDev(double[] residuals) {

        double sum = 0.0;
        int count = 0;

        for (double residual : residuals) {
            if (residual == 0.0) {
                continue;
            }

            sum += residual * residual;
            count++;
        }

        if (count == 0) {
            return 0.0;
        }

        return Math.sqrt(sum / count);
    }
}