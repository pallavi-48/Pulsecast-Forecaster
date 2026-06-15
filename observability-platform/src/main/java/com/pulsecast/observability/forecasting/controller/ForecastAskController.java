package com.pulsecast.observability.forecasting.controller;

import com.pulsecast.observability.forecasting.question.ParsedQuestion;
import com.pulsecast.observability.forecasting.question.QuestionParser;
import com.pulsecast.observability.forecasting.service.ForecastQuestionResponse;
import com.pulsecast.observability.forecasting.service.ForecastQuestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForecastAskController {

    private final QuestionParser questionParser;
    private final ForecastQuestionService forecastQuestionService;

    public ForecastAskController(
            QuestionParser questionParser,
            ForecastQuestionService forecastQuestionService
    ) {
        this.questionParser = questionParser;
        this.forecastQuestionService = forecastQuestionService;
    }

    @GetMapping("/forecast/ask")
    public ForecastQuestionResponse ask(
            @RequestParam("q") String question
    ) {

        ParsedQuestion parsedQuestion =
                questionParser.parse(question);

        return forecastQuestionService.answer(
                parsedQuestion.getMetric(),
                parsedQuestion.getHorizon(),
                parsedQuestion.getDay(),
                parsedQuestion.getQuestionType()
        );
    }
}