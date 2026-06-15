package com.pulsecast.observability.forecasting.analysis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SeasonalityAnalyzer {

    private final AcfCalculator acfCalculator;

    public SeasonalityAnalyzer() {
        this.acfCalculator =
                new AcfCalculator();
    }

    public SeasonalityReport analyze(
            double[] series,
            int maxLag
    ) {

        double[] acf =
                acfCalculator.calculate(
                        series,
                        maxLag
                );

        List<SeasonalityPeak> peaks =
                new ArrayList<>();

        for (int lag = 21;
             lag <= maxLag;
             lag++) {

            peaks.add(
                    new SeasonalityPeak(
                            lag,
                            Math.abs(
                                    acf[lag - 1]
                            )
                    )
            );
        }

        peaks.sort(
                Comparator.comparingDouble(
                        SeasonalityPeak::getCorrelation
                ).reversed()
        );

        List<SeasonalityPeak> topFive =
                peaks.subList(
                        0,
                        Math.min(
                                5,
                                peaks.size()
                        )
                );

        return new SeasonalityReport(
                topFive
        );
    }
}