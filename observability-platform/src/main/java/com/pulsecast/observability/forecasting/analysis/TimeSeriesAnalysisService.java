package com.pulsecast.observability.forecasting.analysis;

import com.pulsecast.observability.forecasting.model.FftPoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimeSeriesAnalysisService {

    public double[] movingAverage(double[] values, int window) {
        double[] result = new double[values.length];

        double sum = 0.0;

        for (int i = 0; i < values.length; i++) {
            sum += values[i];

            if (i >= window) {
                sum -= values[i - window];
            }

            int count = Math.min(i + 1, window);
            result[i] = sum / count;
        }

        return result;
    }

    public double[] residuals(double[] actual, double[] forecast) {
        int length = Math.min(actual.length, forecast.length);
        double[] residuals = new double[length];

        for (int i = 0; i < length; i++) {
            residuals[i] = actual[i] - forecast[i];
        }

        return residuals;
    }

    public List<FftPoint> fftSpectrum(double[] values) {
        int n = largestPowerOfTwo(values.length);

        double[] trimmed = new double[n];

        System.arraycopy(
                values,
                values.length - n,
                trimmed,
                0,
                n
        );

        double[] trend = movingAverage(trimmed, 288);

        double[] detrended = new double[n];

        for (int i = 0; i < n; i++) {
            double hann =
                    0.5 * (1.0 - Math.cos((2.0 * Math.PI * i) / (n - 1)));

            detrended[i] = (trimmed[i] - trend[i]) * hann;
        }

        List<FftPoint> points = new ArrayList<>();

        for (int k = 1; k < n / 2; k++) {
            double real = 0.0;
            double imaginary = 0.0;

            for (int t = 0; t < n; t++) {
                double angle =
                        -2.0 * Math.PI * k * t / n;

                real += detrended[t] * Math.cos(angle);
                imaginary += detrended[t] * Math.sin(angle);
            }

            double power =
                    real * real + imaginary * imaginary;

            double frequency =
                    (double) k / n;

            double periodSamples =
                    1.0 / frequency;

            double periodHours =
                    periodSamples * 5.0 / 60.0;

            if (periodSamples >= 2 && periodSamples <= 2500) {
                points.add(
                        new FftPoint(
                                periodSamples,
                                periodHours,
                                power
                        )
                );
            }
        }

        return points;
    }

    private int largestPowerOfTwo(int value) {
        int power = 1;

        while (power * 2 <= value) {
            power *= 2;
        }

        return power;
    }
}