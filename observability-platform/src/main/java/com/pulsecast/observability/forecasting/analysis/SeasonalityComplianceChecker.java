package com.pulsecast.observability.forecasting.analysis;

public class SeasonalityComplianceChecker {

    public SeasonalityComplianceReport checkCompliance(
            double[] expected,
            double[] actual
    ) {

        int length =
                Math.min(
                        expected.length,
                        actual.length
                );

        double totalError = 0;
        double totalExpected = 0;

        for (int i = 0; i < length; i++) {

            totalError +=
                    Math.abs(
                            expected[i] - actual[i]
                    );

            totalExpected +=
                    Math.abs(expected[i]);
        }

        double errorRatio =
                totalError / totalExpected;

        double complianceScore =
                Math.max(
                        0,
                        100 - (errorRatio * 100)
                );

        boolean compliant =
                complianceScore >= 70;

        return new SeasonalityComplianceReport(
                complianceScore,
                compliant
        );
    }
}