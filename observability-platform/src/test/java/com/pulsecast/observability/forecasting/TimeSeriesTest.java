package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.domain.TimePoint;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeSeriesTest {

    @Test
    void shouldReturnCorrectSize() {

        TimeSeries series = new TimeSeries(
                List.of(
                        new TimePoint(
                                LocalDateTime.now(),
                                100.0,   // requests per second
                                25.0,    // response time
                                0.5,     // error rate
                                60.0,    // cpu
                                70.0,    // memory
                                20.0,    // active threads
                                100.0,   // active users
                                10.0,    // database connections
                                15.0,    // network latency
                                40.0     // disk IO
                        ),
                        new TimePoint(
                                LocalDateTime.now().plusMinutes(5),
                                120.0,
                                30.0,
                                0.7,
                                65.0,
                                75.0,
                                25.0,
                                120.0,
                                12.0,
                                18.0,
                                45.0
                        )
                )
        );

        assertEquals(2, series.size());
    }

    @Test
    void shouldExtractTrafficSeries() {

        TimeSeries series = new TimeSeries(
                List.of(
                        new TimePoint(
                                LocalDateTime.now(),
                                100.0,
                                25.0,
                                0.5,
                                60.0,
                                70.0,
                                20.0,
                                100.0,
                                10.0,
                                15.0,
                                40.0
                        ),
                        new TimePoint(
                                LocalDateTime.now().plusMinutes(5),
                                120.0,
                                30.0,
                                0.7,
                                65.0,
                                75.0,
                                25.0,
                                120.0,
                                12.0,
                                18.0,
                                45.0
                        )
                )
        );

        double[] traffic = series.getTrafficSeries();

        assertEquals(2, traffic.length);
        assertEquals(100.0, traffic[0]);
    }
}