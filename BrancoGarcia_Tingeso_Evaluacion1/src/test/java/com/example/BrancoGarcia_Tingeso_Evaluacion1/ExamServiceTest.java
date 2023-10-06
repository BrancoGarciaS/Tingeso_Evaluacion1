package com.example.BrancoGarcia_Tingeso_Evaluacion1;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.ExamEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories.ExamRepository;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.ExamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
    void saveDataDB(){
        ExamEntity ex = examService.saveDataDB("199.999.999-9",
                "11-03-2020", "990");
        // el código no debería tirar error si el examen se guardó exactamente
        Long id = ex.getId_exam();
        examRepository.deleteById(id);
    }

    @Test
    void deleteAllExams(){
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
    }
}
