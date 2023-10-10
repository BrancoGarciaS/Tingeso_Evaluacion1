package com.example.BrancoGarcia_Tingeso_Evaluacion1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping()
public class HomeController {
    @GetMapping()
    public String showRegister() {
        return "homepage";
    }

    @GetMapping("/search_rut")
    public String searchRut() {
        return "search_rut";
    }
}
