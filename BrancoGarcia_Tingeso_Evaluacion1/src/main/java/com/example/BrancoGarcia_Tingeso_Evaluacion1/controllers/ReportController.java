package com.example.BrancoGarcia_Tingeso_Evaluacion1.controllers;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.ReportEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.ReportService;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping()
public class ReportController {
    @Autowired
    ReportService reportService;

    @Autowired
    StudentService studentService;

    @GetMapping("/report")
    public String report_search_rut(){
        return "reportRut";
    }

    @PostMapping("/report")
    public ModelAndView report_rut(@RequestParam("rut_report") String rut){
        if(studentService.getByRut(rut).isPresent()){
            // si en la base de datos está registrado el estudiante, se muestra su reporte
            ReportEntity r = reportService.createReport(rut);
            ModelAndView modelAndView = new ModelAndView("reportRut");
            modelAndView.addObject("report", r);
            return modelAndView;
        }
        String fail = "Error, rut no registrado";
        ModelAndView modelAndView = new ModelAndView("reportRut");
        modelAndView.addObject("message", fail);
        return modelAndView;
    }


}