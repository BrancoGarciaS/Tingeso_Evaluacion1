package com.example.BrancoGarcia_Tingeso_Evaluacion1.controllers;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.InstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/installment")
public class InstallmentController {
    @Autowired
    InstallmentService installmentService;

    @GetMapping("/search_by_rut")
    public String showRegister() {
        return "installment_rut";
    }

    @PostMapping("/rut")
    @ResponseBody
    public ModelAndView saveStudent(@RequestParam("rut_installment") String rut_installment) {
        ArrayList<InstallmentEntity> c = installmentService.getInstallmentsByRut(rut_installment);
        ModelAndView modelAndView = new ModelAndView("installment_rut");
        modelAndView.addObject("cuotas", c);
        return modelAndView;
    }

}
