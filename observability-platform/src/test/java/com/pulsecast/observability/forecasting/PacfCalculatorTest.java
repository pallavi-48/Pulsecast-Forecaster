package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.PacfCalculator;
import org.junit.jupiter.api.Test;

public class PacfCalculatorTest {

    @Test
    void testPacfCalculation() {

        double[] series = {
                10, 12, 14, 13, 15,
                16, 18, 17, 19, 20
        };

        PacfCalculator pacfCalculator =
                new PacfCalculator();

        double[] pacf =
                pacfCalculator.calculate(
                        series,
                        5
                );

        System.out.println("\nPACF Values\n");

        for (int i = 1; i < pacf.length; i++) {

            System.out.printf(
                    "Lag %d = %.4f%n",
                    i,
                    pacf[i]
            );
        }
    }
}