package com.pulsecast.observability.forecasting.controller;

import com.pulsecast.observability.forecasting.question.ForecastQuestionType;
import com.pulsecast.observability.forecasting.service.ForecastQuestionResponse;
import com.pulsecast.observability.forecasting.service.ForecastQuestionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forecast/question")
public class ForecastQuestionController {

    private final ForecastQuestionService service;

    public ForecastQuestionController(
            ForecastQuestionService service
    ) {
        this.service = service;
    }

    @GetMapping
    public ForecastQuestionResponse ask(
            @RequestParam String metric,
            @RequestParam(defaultValue = "day") String horizon,
            @RequestParam(required = false) String day,
            @RequestParam(defaultValue = "AVERAGE") ForecastQuestionType questionType
    ) {
        return service.answer(
                metric,
                horizon,
                day,
                questionType
        );
    }
}