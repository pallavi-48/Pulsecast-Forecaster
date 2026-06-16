package com.pulsecast.observability.forecasting.model;

public enum DatasetFile {

    OBSERVABILITY_TRAINING(
            "Observability - 2 Months",
            "dataset/2_month_metrics.csv",
            DatasetKind.OBSERVABILITY
    ),

    OBSERVABILITY_CURRENT(
            "Observability - Current Day",
            "dataset/current_metrics.csv",
            DatasetKind.OBSERVABILITY
    ),

    OBSERVABILITY_COMPLIANT(
            "Observability - Compliant Day",
            "dataset/seasonality_compliant_day.csv",
            DatasetKind.OBSERVABILITY
    ),

    OBSERVABILITY_CHAOTIC(
            "Observability - Chaotic Day",
            "dataset/chaotic_unpredictable_day.csv",
            DatasetKind.OBSERVABILITY
    ),

    STOCK_TRAINING(
            "Stock Market - 2 Months",
            "dataset/stock_market_2months.csv",
            DatasetKind.STOCK
    ),

    STOCK_COMPLIANT(
            "Stock - Compliant Day",
            "dataset/stock_compliant_day.csv",
            DatasetKind.STOCK
    ),

    STOCK_CHAOTIC(
            "Stock - Chaotic Day",
            "dataset/stock_chaotic_day.csv",
            DatasetKind.STOCK
    ),

    STOCK_MIXED(
            "Stock - Mixed Day",
            "dataset/stock_mixed_day.csv",
            DatasetKind.STOCK
    ),

    STOCK_LIKE_OBSERVABILITY(
            "Stock Like Observability Day",
            "dataset/stock_like_observability_day.csv",
            DatasetKind.OBSERVABILITY
    ),

    TRAFFIC_TRAINING(
            "Smart City Traffic - 2 Months",
            "dataset/smart_city_traffic_2months.csv",
            DatasetKind.SMART_CITY_TRAFFIC
    ),

    TRAFFIC_COMPLIANT(
            "Traffic - Compliant Day",
            "dataset/traffic_compliant_day.csv",
            DatasetKind.SMART_CITY_TRAFFIC
    ),

    TRAFFIC_CHAOTIC(
            "Traffic - Chaotic Day",
            "dataset/traffic_chaotic_day.csv",
            DatasetKind.SMART_CITY_TRAFFIC
    ),

    TRAFFIC_MIXED(
            "Traffic - Mixed Day",
            "dataset/traffic_mixed_day.csv",
            DatasetKind.SMART_CITY_TRAFFIC
    );

    private final String displayName;
    private final String path;
    private final DatasetKind kind;

    DatasetFile(String displayName, String path, DatasetKind kind) {
        this.displayName = displayName;
        this.path = path;
        this.kind = kind;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPath() {
        return path;
    }

    public DatasetKind getKind() {
        return kind;
    }

    public static DatasetFile fromName(String name) {
        return DatasetFile.valueOf(name.toUpperCase());
    }
}