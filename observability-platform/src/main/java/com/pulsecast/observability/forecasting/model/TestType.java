package com.pulsecast.observability.forecasting.model;

public enum TestType {
    COMPLIANT,
    CHAOTIC,
    MIXED;

    public static TestType from(String value) {
        return TestType.valueOf(value.toUpperCase());
    }
}