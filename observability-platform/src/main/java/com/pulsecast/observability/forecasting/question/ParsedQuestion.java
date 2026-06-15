package com.pulsecast.observability.forecasting.question;

public class ParsedQuestion {

    private final String metric;
    private final String horizon;
    private final String day;
    private final ForecastQuestionType questionType;

    public ParsedQuestion(
            String metric,
            String horizon,
            String day,
            ForecastQuestionType questionType
    ) {
        this.metric = metric;
        this.horizon = horizon;
        this.day = day;
        this.questionType = questionType;
    }

    public String getMetric() {
        return metric;
    }

    public String getHorizon() {
        return horizon;
    }

    public String getDay() {
        return day;
    }

    public ForecastQuestionType getQuestionType() {
        return questionType;
    }
}