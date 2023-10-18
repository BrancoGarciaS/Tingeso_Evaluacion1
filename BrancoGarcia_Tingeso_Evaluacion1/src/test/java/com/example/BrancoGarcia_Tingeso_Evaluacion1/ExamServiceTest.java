package com.example.BrancoGarcia_Tingeso_Evaluacion1;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.ExamEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories.ExamRepository;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories.StudentRepository;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.ExamService;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ExamServiceTest {
    @Autowired
    ExamService examService;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    StudentService studentService;


    @Test
    void saveDataDBTest(){
        ExamEntity ex = examService.saveDataDB("199.999.999-9",
                "11-03-2020", "990");
        // el código no debería tirar error si el examen se guardó exactamente
        Long id = ex.getId_exam();
        assertEquals("199.999.999-9", ex.getRut());
        examRepository.deleteById(id);
    }

    @Test
    void deleteAllExamsTest(){
        // en el caso que la tabla examenes esté vacía se realizará este test
        if(examService.getExams().size() == 0){
            // creo un examen
            ExamEntity ex = examService.saveDataDB("199.999.999-9",
                    "11-03-2020", "990");
            // borro todos los examenes
            examService.deleteAllExams();
            // obtengo la cantidad de examenes ahora
            int n_exams = examService.getExams().size();
            // la cual debería ser 0
            assertEquals(0, n_exams);
        }
        assertTrue(examService.getExams().size() > 0);
    }

    @Test
    void getExams(){
        ExamEntity ex = examService.saveDataDB("199.999.999-9",
                "11-03-2020", "990");
        int n_exams = examService.getExams().size();
        // si funciona getExams debería haber más de 0 examenes
        assertTrue(n_exams > 0);
        examRepository.deleteById(ex.getId_exam());
    }

    @Test
    void getMeanExams(){
        // Se realizará el test si en la base de datos hay 0 examenes guardados
        if(examService.getExams().size() == 0){
            ExamEntity ex1 = examService.saveDataDB("199.999.999-9",
                    "11-03-2020", "1000");
            ExamEntity ex2 = examService.saveDataDB("199.999.999-9",
                    "11-03-2020", "1000");
            List<Object[]> means = examService.getMeanExams();
            Object[] result = means.get(0);
            String rut_test = (String) result[0];
            Double mean_test = (Double) result[1];
            Long num_exams_test = (Long) result[2];
            // el rut del estudiante
            assertEquals("199.999.999-9", rut_test);
            // el promedio debería ser 1000
            assertEquals(1000D, mean_test);
            // solo ingrese dos examenes
            assertEquals(2L, num_exams_test);
            examService.deleteAllExams();
        }
        examService.getMeanExams();
    }
    @Test
    void saveMean(){
        // si no hay examenes en la base de datos
        if(examService.getExams().size() == 0){
            // creo un nuevo estudiante
            StudentEntity s1 = new StudentEntity();
            s1.setRut("199.999.999-9"); s1.setName("Bastian");
            // guardo al estudiante en la base de datos
            // si no está en la base de datos
            if(!studentService.getByRut("199.999.999-9").isPresent()){
                StudentEntity sv1 = studentService.saveStudents(s1);
                examService.saveDataDB("199.999.999-9",
                        "11-03-2020", "990");
                examService.saveDataDB("199.999.999-9",
                        "11-03-2020", "1000");
                examService.saveDataDB("199.999.999-9",
                        "11-03-2020", "950");
                examService.saveMean();

                Long num_exams1 = sv1.getNum_exams();
                assertEquals(3,num_exams1);
                float score1 = sv1.getScore();
                float real_score = (990 + 1000 + 950)/3;
                assertEquals(real_score, score1);
                studentService.deleteById(sv1);
                examService.deleteAllExams(); // borro todos los examenes
            }
        }
        examService.saveMean();
    }

    @Test
    void readCsvTest(){
        examService.readCsv("archivo_no_existente");
    }
    /*
    @Test
    void read(){
        examService.getMeanExams();
    }
     */


}
