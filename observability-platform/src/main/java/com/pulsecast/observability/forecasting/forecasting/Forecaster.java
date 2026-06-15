package com.pulsecast.observability.forecasting.forecasting;

import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;

public interface Forecaster {

    ForecastResult forecast(
            SarimaModel model,
            double[] series,
            int horizon
    );
}