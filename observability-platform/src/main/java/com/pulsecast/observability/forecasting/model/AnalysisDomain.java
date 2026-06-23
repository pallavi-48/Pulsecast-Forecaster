package com.pulsecast.observability.forecasting.model;

public enum AnalysisDomain {

    OBSERVABILITY(
            "dataset/2_month_metrics.csv",
            "dataset/seasonality_compliant_day.csv",
            "dataset/chaotic_unpredictable_day.csv",
            "dataset/current_metrics.csv"
    ),

    STOCK(
            "dataset/stock_market_2months.csv",
            "dataset/stock_compliant_day.csv",
            "dataset/stock_chaotic_day.csv",
            "dataset/stock_mixed_day.csv"
    ),

    TRAFFIC(
            "dataset/smart_city_traffic_2months.csv",
            "dataset/traffic_compliant_day.csv",
            "dataset/traffic_chaotic_day.csv",
            "dataset/traffic_mixed_day.csv"
    ),

    SOLAR(
            "dataset/solar_power_2months.csv",
            "dataset/solar_compliant_day.csv",
            "dataset/solar_chaotic_day.csv",
            "dataset/solar_mixed_day.csv"
    );

    private final String trainingPath;
    private final String compliantPath;
    private final String chaoticPath;
    private final String mixedPath;

    AnalysisDomain(
            String trainingPath,
            String compliantPath,
            String chaoticPath,
            String mixedPath
    ) {
        this.trainingPath = trainingPath;
        this.compliantPath = compliantPath;
        this.chaoticPath = chaoticPath;
        this.mixedPath = mixedPath;
    }

    public String getTrainingPath() {
        return trainingPath;
    }

    public String getTestPath(TestType testType) {
        return switch (testType) {
            case COMPLIANT -> compliantPath;
            case CHAOTIC -> chaoticPath;
            case MIXED -> mixedPath;
        };
    }

    public static AnalysisDomain from(String value) {
        return AnalysisDomain.valueOf(value.toUpperCase());
    }
}