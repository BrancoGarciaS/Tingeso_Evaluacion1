package com.example.BrancoGarcia_Tingeso_Evaluacion1;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.ReportEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories.ReportRepository;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.InstallmentService;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.ReportService;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReportServiceTest {
    @Autowired
    ReportService reportService;

    @Autowired
    StudentService studentService;

    @Autowired
    InstallmentService installmentService;

    @Autowired
    ReportRepository reportRepository;

    @Test
    void createReport(){
        LocalDate birth_date = LocalDate.of(2000, 3, 22);
        StudentEntity sv = studentService.saveDataStudents("Bastian", "Castro",
                "bastiancastro@gmail.com", "1.999.999.999-K",
                birth_date, 2022, "SQL",
                1L, 2);
        // creo el reporte
        ReportEntity report = reportService.createReport("1.999.999.999-K");
        // en teoría en el reporte debería ser el tipo de pago 1 (en cuotas)
        Integer payment_type_test = report.getPayment_type();
        assertEquals(1, payment_type_test);
        // en teoría el número de cuotas pagadas debería ser 0
        Integer n = report.getNum_installments_paid();
        assertEquals(0,n);
        // en teoría el monto total pagado debería ser 0
        float paid = report.getTariff_paid();
        assertEquals(0, paid);
        // en teoría la fecha del último pago es un valor nulo
        LocalDate l = report.getLast_payment();
        assertNull(l);

        // borro reporte
        reportRepository.deleteById(report.getId_report());
        // borro las cuotas asociadas
        ArrayList<InstallmentEntity> ins = installmentService.getInstallmentsByRut("1.999.999.999-K");
        for(InstallmentEntity i : ins){
            installmentService.deleteById(i);
        }
        // borro al estudiante
        studentService.deleteById(sv);
    }

    @Test
    void updateReport(){
        LocalDate birth_date = LocalDate.of(2000, 3, 22);
        StudentEntity sv = studentService.saveDataStudents("Bastian", "Castro",
                "bastiancastro@gmail.com", "1.999.999.999-K",
                birth_date, 2022, "SQL",
                1L, 2);
        // una vez creado el estudiante con sus dos cuotas, agregaré manualmente una
        InstallmentEntity newIns = new InstallmentEntity();
        newIns.setRut_installment("1.999.999.999-K");
        newIns.setInstallmentState(1); // pagada
        newIns.setPayment_amount(250000f);
        LocalDate payment_date = LocalDate.of(2023,1,4);
        LocalDate due_date = LocalDate.of(2023,1,10);
        LocalDate start_date = LocalDate.of(2023,1,5);
        newIns.setDue_date(due_date);
        newIns.setStart_date(start_date);
        newIns.setPayment_date(payment_date);
        // nueva cuota asociada al estudiante
        InstallmentEntity is_new = installmentService.saveData(newIns);
        // creo el reporte
        ReportEntity report = reportService.createReport("1.999.999.999-K");
        // en teoría en el reporte debería ser el tipo de pago 1 (en cuotas)
        Integer payment_type_test = report.getPayment_type();
        assertEquals(1, payment_type_test);
        ReportEntity report2 = reportService.updateReport(report,sv);
        // en teoría el número de cuotas pagadas debería ser 1 ahora con la actualización
        Integer n = report2.getNum_installments_paid();
        assertEquals(1,n);
        // en teoría el monto total pagado debería ser los 250000f
        float paid = report2.getTariff_paid();
        assertEquals(250000f, paid);

        // borro reporte
        reportRepository.deleteById(report2.getId_report());
        // borro las cuotas asociadas
        ArrayList<InstallmentEntity> ins = installmentService.getInstallmentsByRut("1.999.999.999-K");
        for(InstallmentEntity i : ins){
            installmentService.deleteById(i);
        }
        // borro al estudiante
        studentService.deleteById(sv);
    }
}
