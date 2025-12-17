package com.emir.mytasks.mytasks_backend.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // Siteye giren herkesi direkt login paneline yönlendir
        return "redirect:/login";
    }
}