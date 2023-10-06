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


    /*
    @PostMapping("/search_rut") // Agrega un método POST para /search_rut
    public String processForm(@RequestParam("rut_installment") String rut, @RequestParam("action") String action) {
        if ("buscarCuotas".equals(action)) {
            // Realiza la acción de buscar cuotas por rut
            return "redirect:/installment/rut?rut=" + rut;
        } else if ("generarReporte".equals(action)) {
            // Realizar la acción de generar reporte con el rut
            return "redirect:/report?rut=" + rut;
        }

        return "redirect:/"; // Redirigir a una página por defecto si no se especifica una acción válida
    } */
}
