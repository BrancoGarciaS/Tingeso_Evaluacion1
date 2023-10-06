package com.example.BrancoGarcia_Tingeso_Evaluacion1.controllers;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.InstallmentService;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.InstallmentsCalculation;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class StudentController {
    @Autowired
    StudentService studentService;

    @Autowired
    InstallmentsCalculation installmentsCalculation;

    @Autowired
    InstallmentService installmentService;

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    // Para obtener todos los estudiantes
    @GetMapping("/get_students")
    @ResponseBody
    public ArrayList<StudentEntity> getStudents(){
        return studentService.getStudents();
    }


    // Para registrar a un estudiante
    @PostMapping("/register_student")
    @ResponseBody
    public ModelAndView saveStudent(@RequestParam("name") String name,
                                     @RequestParam("last_name") String lastName,
                                     @RequestParam("email") String email,
                                     @RequestParam("rut") String rut,
                                     @RequestParam("birth_date") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                         LocalDate birth_date,
                                     @RequestParam("senior_year") int senior_year,
                                     @RequestParam("school_name") String schoolName,
                                     @RequestParam("school_type") Long schoolType,
                                     @RequestParam("installments") Integer installments) {
        StudentEntity s = studentService.saveDataStudents(name, lastName, email, rut,
                birth_date, senior_year, schoolName, schoolType, installments);
        if(s == null){
            ModelAndView m = new ModelAndView("error_register");
            return m;
        }
        ModelAndView modelAndView = new ModelAndView("exit_register");
        modelAndView.addObject("student", s);
        return modelAndView;
    }

    @GetMapping("/search_details")
    public String details(){
        return "detailsRut";
    }

    @PostMapping("/student_details")
    @ResponseBody
    public ModelAndView detailsStudent(@RequestParam("student_rut") String rut){
        ModelAndView m = new ModelAndView("detailsRut");
        if(studentService.getByRut(rut).isPresent()){ // si el estudiante está registrado
            StudentEntity s = studentService.getByRut(rut).get();
            m.addObject("student", s);
            return m;
        }
        String fail = "Error, estudiante no registrado";
        m.addObject("error", fail);
        return m;
    }

}
