package com.emir.mytasks.mytasks_backend.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        //irekt admin girişine yolla
        return "redirect:/admin/login";
    }
}