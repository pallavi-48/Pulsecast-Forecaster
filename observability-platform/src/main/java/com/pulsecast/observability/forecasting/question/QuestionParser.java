package com.pulsecast.observability.forecasting.question;

import org.springframework.stereotype.Component;

@Component
public class QuestionParser {

    public ParsedQuestion parse(String question) {

        String q =
                question == null
                        ? ""
                        : question.toLowerCase();

        String metric =
                detectMetric(q);

        String day =
                detectDay(q);

        String horizon =
                detectHorizon(q, day);

        ForecastQuestionType questionType =
                detectQuestionType(q);

        return new ParsedQuestion(
                metric,
                horizon,
                day,
                questionType
        );
    }

    private String detectMetric(String question) {

        if (question.contains("latency")
                || question.contains("response time")
                || question.contains("slow")) {
            return "latency";
        }

        if (question.contains("error")
                || question.contains("errors")
                || question.contains("failure")
                || question.contains("failures")) {
            return "errors";
        }

        if (question.contains("cpu")
                || question.contains("saturation")
                || question.contains("resource")
                || question.contains("load")) {
            return "saturation";
        }

        return "traffic";
    }

    private String detectDay(String question) {

        if (question.contains("monday")) {
            return "monday";
        }

        if (question.contains("tuesday")) {
            return "tuesday";
        }

        if (question.contains("wednesday")) {
            return "wednesday";
        }

        if (question.contains("thursday")) {
            return "thursday";
        }

        if (question.contains("friday")) {
            return "friday";
        }

        if (question.contains("saturday")) {
            return "saturday";
        }

        if (question.contains("sunday")) {
            return "sunday";
        }

        if (question.contains("tomorrow")
                || question.contains("next day")) {
            return "monday";
        }

        return null;
    }

    private String detectHorizon(
            String question,
            String day
    ) {

        if (question.contains("week")
                || question.contains("next week")) {
            return "week";
        }

        if (day != null) {
            return "day";
        }

        return "day";
    }

    private ForecastQuestionType detectQuestionType(
            String question
    ) {

        if (question.contains("peak")
                || question.contains("highest")
                || question.contains("maximum")
                || question.contains("max")) {
            return ForecastQuestionType.PEAK;
        }

        if (question.contains("lowest")
                || question.contains("minimum")
                || question.contains("min")) {
            return ForecastQuestionType.MINIMUM;
        }

        if (question.contains("increase")
                || question.contains("decrease")
                || question.contains("trend")
                || question.contains("rise")
                || question.contains("fall")
                || question.contains("spike")) {
            return ForecastQuestionType.TREND;
        }

        if (question.contains("anomaly")
                || question.contains("anomalies")
                || question.contains("abnormal")
                || question.contains("unusual")) {
            return ForecastQuestionType.ANOMALY;
        }

        if (question.contains("seasonality")
                || question.contains("seasonal")
                || question.contains("compliance")
                || question.contains("normal pattern")) {
            return ForecastQuestionType.COMPLIANCE;
        }

        return ForecastQuestionType.AVERAGE;
    }
}