package com.pulsecast.observability.forecasting.evaluation;

public class AnomalyPoint {

    private final int index;
    private final double actual;
    private final double predicted;
    private final double absoluteError;
    private final double percentageError;
    private final AnomalySeverity severity;

    public AnomalyPoint(
            int index,
            double actual,
            double predicted,
            double absoluteError,
            double percentageError,
            AnomalySeverity severity
    ) {
        this.index = index;
        this.actual = actual;
        this.predicted = predicted;
        this.absoluteError = absoluteError;
        this.percentageError = percentageError;
        this.severity = severity;
    }

    public int getIndex() {
        return index;
    }

    public double getActual() {
        return actual;
    }

    public double getPredicted() {
        return predicted;
    }

    public double getAbsoluteError() {
        return absoluteError;
    }

    public double getPercentageError() {
        return percentageError;
    }

    public AnomalySeverity getSeverity() {
        return severity;
    }
}