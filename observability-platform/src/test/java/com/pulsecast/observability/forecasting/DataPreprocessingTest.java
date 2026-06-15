package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.preprocessing.DifferencingTransformer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataPreprocessingTest {

    @Test
    void shouldDifferenceSeries() {

        DifferencingTransformer transformer =
                new DifferencingTransformer();

        double[] values =
                {100, 110, 120, 130};

        double[] result =
                transformer.difference(values);

        assertEquals(
                10,
                result[0]
        );

        assertEquals(
                10,
                result[1]
        );

        assertEquals(
                10,
                result[2]
        );
    }
}