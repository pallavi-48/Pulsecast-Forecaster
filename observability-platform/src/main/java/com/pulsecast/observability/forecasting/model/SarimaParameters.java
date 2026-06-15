package com.pulsecast.observability.forecasting.model;

public class SarimaParameters {

    private final int p;
    private final int d;
    private final int q;

    private final int P;
    private final int D;
    private final int Q;

    private final int s;

    public SarimaParameters(
            int p,
            int d,
            int q,
            int P,
            int D,
            int Q,
            int s
    ) {
        this.p = p;
        this.d = d;
        this.q = q;

        this.P = P;
        this.D = D;
        this.Q = Q;

        this.s = s;
    }

    public int getP() { return p; }

    public int getD() { return d; }

    public int getQ() { return q; }

    public int getSeasonalP() { return P; }

    public int getSeasonalD() { return D; }

    public int getSeasonalQ() { return Q; }

    public int getSeasonLength() { return s; }
}