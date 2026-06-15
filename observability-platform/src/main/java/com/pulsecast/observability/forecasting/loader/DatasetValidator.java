package com.pulsecast.observability.forecasting.loader;

import com.pulsecast.observability.forecasting.domain.TimeSeries;

public class DatasetValidator {

    public void validate(
            TimeSeries series
    ) {

        if (series == null) {

            throw new IllegalArgumentException(
                    "Dataset is null"
            );
        }

        if (series.isEmpty()) {

            throw new IllegalArgumentException(
                    "Dataset is empty"
            );
        }

        if (series.size() < 100) {

            throw new IllegalArgumentException(
                    "Dataset too small for forecasting"
            );
        }
    }
}