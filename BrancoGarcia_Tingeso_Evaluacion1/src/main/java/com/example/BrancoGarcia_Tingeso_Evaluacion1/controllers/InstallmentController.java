package com.example.BrancoGarcia_Tingeso_Evaluacion1.controllers;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.InstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if(c.size() == 0){
            ModelAndView modelAndView = new ModelAndView("rut_not_found");
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("installment_rut");
        modelAndView.addObject("cuotas", c);
        return modelAndView;
    }

    @PostMapping("/pay")
    public ModelAndView pay_installment(@RequestParam("installmentId") Long installmentId) {
        // Obtener la cuota por su ID desde el servicio
        Optional<InstallmentEntity> installment = installmentService.getInstallmentById(installmentId);
        if (!installment.isPresent()) {
            // Manejar el caso en el que la cuota no existe (puedes redirigir o mostrar un mensaje de error)
            ModelAndView modelAndView = new ModelAndView("rut_not_found");
            return modelAndView;
        }
        // Pago la cuota
        installmentService.pay_installment(installment);
        // Vuelvo a mostrar las cuotas de dicho usuario
        ArrayList<InstallmentEntity> c = installmentService.getInstallmentsByRut(installment.get().getRut_installment());
        ModelAndView modelAndView = new ModelAndView("installment_rut");
        modelAndView.addObject("cuotas", c);
        return modelAndView;
    }

}
