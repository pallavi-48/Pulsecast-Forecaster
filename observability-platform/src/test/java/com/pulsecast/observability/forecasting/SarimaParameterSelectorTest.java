package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.model.SarimaParameters;

import org.junit.jupiter.api.Test;

public class SarimaParameterSelectorTest {

    @Test
    void testParameterSelection() {

        SarimaParameterSelector selector =
                new SarimaParameterSelector();

        SarimaParameters params =
                selector.selectParameters();

        System.out.println(
                "\n===== SARIMA PARAMETERS =====\n"
        );

        System.out.println(
                "p = " + params.getP()
        );

        System.out.println(
                "d = " + params.getD()
        );

        System.out.println(
                "q = " + params.getQ()
        );

        System.out.println(
                "P = " + params.getSeasonalP()
        );

        System.out.println(
                "D = " + params.getSeasonalD()
        );

        System.out.println(
                "Q = " + params.getSeasonalQ()
        );

        System.out.println(
                "s = " + params.getSeasonLength()
        );
    }
}