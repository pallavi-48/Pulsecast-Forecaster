package com.pulsecast.observability.forecasting.analysis;

import com.pulsecast.observability.forecasting.model.SarimaParameters;

public class SarimaParameterSelector {

    public SarimaParameters selectParameters() {

        int p = 1;
        int d = 1;
        int q = 1;

        int P = 1;
        int D = 1;
        int Q = 1;

        int s = 288;

        return new SarimaParameters(
                p,
                d,
                q,
                P,
                D,
                Q,
                s
        );
    }
}