package com.pulsecast.observability.forecasting.analysis;

import com.pulsecast.observability.forecasting.model.DominantPeriod;
import com.pulsecast.observability.forecasting.model.FftSpectrumPoint;
import com.pulsecast.observability.forecasting.model.SeasonalityStrengthResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FftAnalysisService {

    private static final int DAILY_PERIOD = 288;
    private static final int WEEKLY_PERIOD = 2016;

    public List<FftSpectrumPoint> calculateSpectrum(
            double[] values
    ) {
        int n =
                largestPowerOfTwo(values.length);

        double[] series =
                new double[n];

        System.arraycopy(
                values,
                values.length - n,
                series,
                0,
                n
        );

        double[] detrended =
                detrendUsingMovingAverage(
                        series,
                        DAILY_PERIOD
                );

        double[] windowed =
                applyHannWindow(

                        detrended
                );

        List<RawPowerPoint> rawPower =
                new ArrayList<>();

        double totalPower =
                0.0;

        for (int k = 1; k < n / 2; k++) {

            double real =
                    0.0;

            double imaginary =
                    0.0;

            for (int t = 0; t < n; t++) {

                double angle =
                        -2.0 * Math.PI * k * t / n;

                real +=
                        windowed[t] * Math.cos(angle);

                imaginary +=
                        windowed[t] * Math.sin(angle);
            }

            double power =
                    real * real + imaginary * imaginary;

            double periodSamples =
                    (double) n / k;

            double periodHours =
                    periodSamples * 5.0 / 60.0;

            if (periodSamples >= 2 && periodSamples <= 2500) {

                rawPower.add(
                        new RawPowerPoint(
                                k,
                                periodSamples,
                                periodHours,
                                power
                        )
                );

                totalPower +=
                        power;
            }
        }

        List<FftSpectrumPoint> result =
                new ArrayList<>();

        for (RawPowerPoint point : rawPower) {

            double percentage =
                    totalPower == 0.0
                            ? 0.0
                            : (point.power / totalPower) * 100.0;

            result.add(
                    new FftSpectrumPoint(
                            point.binIndex,
                            point.periodSamples,
                            point.periodHours,
                            point.power,
                            percentage
                    )
            );
        }

        return result;
    }

    public SeasonalityStrengthResponse calculateSeasonalityStrength(
            String domain,
            String metric,
            double[] values
    ) {
        List<FftSpectrumPoint> spectrum =
                calculateSpectrum(values);

        double dailyStrength =
                powerNearPeriod(
                        spectrum,
                        DAILY_PERIOD,
                        20
                );

        double weeklyStrength =
                powerNearPeriod(
                        spectrum,
                        WEEKLY_PERIOD,
                        80
                );

        double harmonicStrength =
                powerNearPeriod(
                        spectrum,
                        DAILY_PERIOD / 2,
                        15
                )
                        + powerNearPeriod(
                        spectrum,
                        DAILY_PERIOD / 3,
                        10
                )
                        + powerNearPeriod(
                        spectrum,
                        DAILY_PERIOD / 4,
                        8
                );

        double seasonalExplained =
                dailyStrength
                        + weeklyStrength
                        + harmonicStrength;

        seasonalExplained =
                Math.min(
                        seasonalExplained,
                        100.0
                );

        double nonPeriodic =
                100.0 - seasonalExplained;

        List<DominantPeriod> dominantPeriods =
                findLocalPeakPeriods(
                        spectrum
                );

        return new SeasonalityStrengthResponse(
                domain,
                metric,
                dailyStrength,
                weeklyStrength,
                harmonicStrength,
                seasonalExplained,
                nonPeriodic,
                dominantPeriods
        );
    }

    private double powerNearPeriod(
            List<FftSpectrumPoint> spectrum,
            int targetPeriod,
            int tolerance
    ) {
        double sum =
                0.0;

        for (FftSpectrumPoint point : spectrum) {

            if (Math.abs(point.periodSamples - targetPeriod) <= tolerance) {

                sum +=
                        point.percentageOfTotalPower;
            }
        }

        return sum;
    }

    private List<DominantPeriod> findLocalPeakPeriods(
            List<FftSpectrumPoint> spectrum
    ) {
        List<FftSpectrumPoint> peaks =
                new ArrayList<>();

        for (int i = 1; i < spectrum.size() - 1; i++) {

            FftSpectrumPoint previous =
                    spectrum.get(i - 1);

            FftSpectrumPoint current =
                    spectrum.get(i);

            FftSpectrumPoint next =
                    spectrum.get(i + 1);

            boolean isLocalPeak =
                    current.power > previous.power
                            && current.power > next.power;

            boolean usefulPeriodRange =
                    current.periodHours >= 2.0
                            && current.periodHours <= 200.0;

            if (isLocalPeak && usefulPeriodRange) {

                peaks.add(
                        current
                );
            }
        }

        return peaks.stream()
                .sorted(
                        Comparator.comparingDouble(
                                (FftSpectrumPoint point) -> point.power
                        ).reversed()
                )
                .limit(10)
                .map(point -> new DominantPeriod(
                        point.periodSamples,
                        point.periodHours,
                        point.power,
                        point.percentageOfTotalPower
                ))
                .toList();
    }

    private double[] detrendUsingMovingAverage(
            double[] values,
            int window
    ) {
        double[] trend =
                movingAverage(
                        values,
                        window
                );

        double[] detrended =
                new double[values.length];

        for (int i = 0; i < values.length; i++) {

            detrended[i] =
                    values[i] - trend[i];
        }

        return detrended;
    }

    private double[] movingAverage(
            double[] values,
            int window
    ) {
        double[] result =
                new double[values.length];

        double sum =
                0.0;

        for (int i = 0; i < values.length; i++) {

            sum +=
                    values[i];

            if (i >= window) {

                sum -=
                        values[i - window];
            }

            int count =
                    Math.min(
                            i + 1,
                            window
                    );

            result[i] =
                    sum / count;
        }

        return result;
    }

    private double[] applyHannWindow(
            double[] values
    ) {
        double[] result =
                new double[values.length];

        int n =
                values.length;

        for (int i = 0; i < n; i++) {

            double hann =
                    0.5 * (
                            1.0 - Math.cos(
                                    (2.0 * Math.PI * i) / (n - 1)
                            )
                    );

            result[i] =
                    values[i] * hann;
        }

        return result;
    }

    private int largestPowerOfTwo(
            int value
    ) {
        int power =
                1;

        while (power * 2 <= value) {

            power *=
                    2;
        }

        return power;
    }

    private static class RawPowerPoint {

        private final int binIndex;

        private final double periodSamples;

        private final double periodHours;

        private final double power;

        private RawPowerPoint(
                int binIndex,
                double periodSamples,
                double periodHours,
                double power
        ) {
            this.binIndex =
                    binIndex;

            this.periodSamples =
                    periodSamples;

            this.periodHours =
                    periodHours;

            this.power =
                    power;
        }
    }
}