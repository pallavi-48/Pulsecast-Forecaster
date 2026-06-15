package com.pulsecast.observability.forecasting.analysis;

import java.util.List;

public class SeasonalityReport {

    private final List<SeasonalityPeak> topPeaks;

    public SeasonalityReport(
            List<SeasonalityPeak> topPeaks
    ) {
        this.topPeaks = topPeaks;
    }

    public List<SeasonalityPeak> getTopPeaks() {
        return topPeaks;
    }
}