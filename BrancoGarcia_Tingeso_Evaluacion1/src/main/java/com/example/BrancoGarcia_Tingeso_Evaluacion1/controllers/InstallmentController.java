package com.example.BrancoGarcia_Tingeso_Evaluacion1.controllers;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.InstallmentService;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.StudentService;
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

    @Autowired
    StudentService studentService;

    @GetMapping("/search_by_rut")
    public String showRegister() {
        return "installment_rut";
    }

    @PostMapping("/rut")
    @ResponseBody
    public ModelAndView getInstallments(@RequestParam("rut_installment") String rut) {
        installmentService.apply_interest(); // aplico intereses si hay atrasos
        ArrayList<InstallmentEntity> c = installmentService.getInstallmentsByRut(rut);
        if(!studentService.getByRut(rut).isPresent()){ // si no está registrado el usuario
            String fail = "Usuario no registrado";
            ModelAndView modelAndView = new ModelAndView("installment_rut");
            modelAndView.addObject("message_fail", fail);
            return modelAndView;
        }
        else if(c.size() == 0){ // en caso que no tenga cuotas
            String fail = "Usuario sin cuotas asociadas (pagó al contado)";
            ModelAndView modelAndView = new ModelAndView("installment_rut");
            modelAndView.addObject("message_fail", fail);
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("installment_rut");
        modelAndView.addObject("cuotas", c);
        return modelAndView;
    }

    @PostMapping("/pay")
    public ModelAndView pay_installment(@RequestParam("installmentId") Long installmentId) {
        installmentService.apply_interest(); // aplico intereses si hay atrasos
        // Obtener la cuota por su ID desde el servicio
        Optional<InstallmentEntity> installment = installmentService.getInstallmentById(installmentId);
        if (!installment.isPresent()) {
            // Manejar el caso en el que la cuota no existe (puedes redirigir o mostrar un mensaje de error)
            ModelAndView modelAndView = new ModelAndView("rut_not_found");
            return modelAndView;
        }
        // Pago la cuota
        int exit_pay = installmentService.pay_installment(installment);
        // Vuelvo a mostrar las cuotas de dicho usuario
        ArrayList<InstallmentEntity> c = installmentService.getInstallmentsByRut(installment.get().getRut_installment());
        ModelAndView modelAndView = new ModelAndView("installment_rut");
        modelAndView.addObject("cuotas", c);
        if(exit_pay == 1){
            String exit = "Pago realizado con éxito";
            modelAndView.addObject("message", exit);
        }
        else{
            String fail = "Pago rechazado (los pagos se realizan entre el 5 y 10 de cada mes)";
            modelAndView.addObject("message", fail);
        }
        return modelAndView;
    }

    @GetMapping("/interest")
    public void updateInterest(){
        installmentService.interest();
    }

}
