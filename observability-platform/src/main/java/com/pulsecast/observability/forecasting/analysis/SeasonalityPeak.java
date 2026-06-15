package com.pulsecast.observability.forecasting.analysis;

public class SeasonalityPeak {

    private final int lag;
    private final double correlation;

    public SeasonalityPeak(
            int lag,
            double correlation
    ) {
        this.lag = lag;
        this.correlation = correlation;
    }

    public int getLag() {
        return lag;
    }

    public double getCorrelation() {
        return correlation;
    }
}