package com.pulsecast.observability.forecasting.analysis;

public class SeasonalityComplianceReport {

    private final double complianceScore;
    private final boolean compliant;

    public SeasonalityComplianceReport(
            double complianceScore,
            boolean compliant
    ) {
        this.complianceScore = complianceScore;
        this.compliant = compliant;
    }

    public double getComplianceScore() {
        return complianceScore;
    }

    public boolean isCompliant() {
        return compliant;
    }
}