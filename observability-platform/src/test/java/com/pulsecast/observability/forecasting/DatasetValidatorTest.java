package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.DatasetValidator;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatasetValidatorTest {

    @Test
    void shouldValidateHistoricalDataset() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadHistorical();

        DatasetValidator validator =
                new DatasetValidator();

        assertDoesNotThrow(
                () -> validator.validate(series)
        );
    }
}