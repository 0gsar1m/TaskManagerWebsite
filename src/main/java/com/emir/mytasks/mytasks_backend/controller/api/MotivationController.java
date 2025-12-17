package com.emir.mytasks.mytasks_backend.controller.api;

import com.emir.mytasks.mytasks_backend.dto.MotivationQuoteResponse;
import com.emir.mytasks.mytasks_backend.service.MotivationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/motivation")
public class MotivationController {

    private final MotivationService motivationService;

    public MotivationController(MotivationService motivationService) {
        this.motivationService = motivationService;
    }

    @GetMapping
    public MotivationQuoteResponse getMotivation() {
        return motivationService.getDailyQuote();
    }
}
