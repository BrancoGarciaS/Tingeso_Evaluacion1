package com.example.BrancoGarcia_Tingeso_Evaluacion1.controllers;

import ch.qos.logback.core.model.Model;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.ExamEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    ExamService examService;
    @GetMapping("/excel_exams")
    public String excel(){
        return "excel_page";
    }

    @PostMapping("/load_excel")
    public String subirExcel(@RequestParam("file") MultipartFile file){
        examService.saveFile(file);
        String filename = file.getOriginalFilename();
        examService.readCsv(filename);
        return "excel_page";
    }

    @GetMapping("/load_mean")
    public ModelAndView loadMean(){
        List<Object[]> results = examService.getMeanExams();
        ModelAndView m = new ModelAndView("exams_mean");
        m.addObject("results", results);
        return m;
    }

    @GetMapping("/save_mean")
    public String saveMean(){
        List<Object[]> results = examService.saveMean();
        examService.deleteAllExams(); // borro todos los examenes de la base de datos
        return "mean_exit";
    }

}
