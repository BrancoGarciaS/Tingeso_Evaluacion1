package com.example.BrancoGarcia_Tingeso_Evaluacion1.controllers;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class StudentController {
    @Autowired
    StudentService studentService;

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
    public StudentEntity saveStudent(Model model, @RequestParam("name") String name,
                                     @RequestParam("last_name") String lastName,
                                     @RequestParam("email") String email,
                                     @RequestParam("rut") String rut,
                                     @RequestParam("birth_date") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                         Date birth_date,
                                     @RequestParam("senior_year") int senior_year,
                                     @RequestParam("school_name") String schoolName,
                                     @RequestParam("school_type") Long schoolType,
                                     @RequestParam("installments") Integer installments) {
        StudentEntity student = new StudentEntity();
        // guardo la información en student
        student.setName(name);
        student.setLast_name(lastName);
        student.setEmail(email);
        student.setRut(rut);
        student.setBirthday(birth_date); // fecha de nacimiento
        student.setSenior_year(senior_year);
        student.setSchool_name(schoolName);
        student.setSchool_type(schoolType);
        student.setNum_exams(0); // al principio tendría 0 examenes rendidos

        if(installments > 0){
            student.setPayment_type(1); // pago en cuotas
        }
        if(installments == 0){
            student.setPayment_type(0); // pago al contado
        }
        // tipo de pago (iniciara por defecto como contado)
        if(schoolType == 2 && installments > 7){
            student.setInstallments(7);
        }
        else if(schoolType == 3 && installments > 4){
            student.setInstallments(4);
        }
        else{
            student.setInstallments(installments); // iniciará con 0 cuotas pagas en total
        }

        student.setTariff(1500000); // valor del arancel (cambiara esto cuando haga historia 2)
        student.setExams_mean(0); // promedio de examenes

        return studentService.saveStudents(student);
    }


}
