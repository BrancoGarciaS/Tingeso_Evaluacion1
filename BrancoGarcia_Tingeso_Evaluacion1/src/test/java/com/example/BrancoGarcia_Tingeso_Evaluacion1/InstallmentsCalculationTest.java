package com.example.BrancoGarcia_Tingeso_Evaluacion1;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.InstallmentsCalculation;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class InstallmentsCalculationTest {

    @Autowired
    InstallmentsCalculation installmentsCalculation;

    @Test
    void testSchoolTypeDiscount(){
        StudentEntity s = new StudentEntity();
        s.setRut("19.999.999-9"); s.setName("Bastian");
        s.setLast_name("Castro"); s.setEmail("bastiancastro@gmail.com");
        s.setAge(19); s.setSchool_name("Liceo Andrés Bello");
        s.setSchool_type(1L);  // el tipo de escuela es municipal
        s.setSenior_year(2022);
        s.setNum_exams(2L); s.setScore(903.4f);
        s.setPayment_type(1); s.setNum_installments(3);
        s.setTariff(1500000); // precio original del arancel
        s.setTuition(70000);
        Integer discount = installmentsCalculation.schoolTypeDiscount(s);
        Integer correct = 300000; // 1.500.000 * 0.2 = 300.000
        assertEquals(correct, discount);
    }

    @Test
    void testSeniorYearDiscount(){
        StudentEntity s = new StudentEntity();
        s.setRut("19.999.999-9"); s.setName("Bastian");
        s.setLast_name("Castro"); s.setEmail("bastiancastro@gmail.com");
        s.setAge(19); s.setSchool_name("Liceo Andrés Bello");
        s.setSchool_type(1L);
        s.setSenior_year(2022); // se egresó del año 2022
        s.setNum_exams(2L); s.setScore(903.4f);
        s.setPayment_type(1); s.setNum_installments(3);
        s.setTariff(1500000); // precio original del arancel
        s.setTuition(70000);
        Integer discount = installmentsCalculation.seniorYearDiscount(s);
        Integer correct = 120000; // 1.500.000 * 0.08 = 120.000
        assertEquals(correct, discount);
    }

    @Test
    void testDiscount_tariff(){
        StudentEntity s = new StudentEntity();
        s.setRut("19.999.999-9"); s.setName("Bastian");
        s.setLast_name("Castro"); s.setEmail("bastiancastro@gmail.com");
        s.setAge(19); s.setSchool_name("Liceo Andrés Bello");
        s.setSchool_type(2L); // el tipo de escuela es subvencionado
        s.setSenior_year(2023); // se egresó del año 2023
        s.setNum_exams(2L); s.setScore(903.4f);
        s.setPayment_type(1); s.setNum_installments(3);
        s.setTariff(1500000); // precio original del arancel
        s.setTuition(70000);
        Integer tariff = 1500000; // arancel total sin descuento
        Integer schoolTypeDiscount = (int) (tariff * 0.1); // 150.000
        Integer schoolYearDiscount = (int) (tariff * 0.15); // 225.000
        Integer correctTariffDiscount = tariff - schoolYearDiscount - schoolTypeDiscount; // 1.125.000
        Integer tariffDiscount = installmentsCalculation.discount_tariff(s);
        assertEquals(correctTariffDiscount,tariffDiscount);
    }

    @Test
    void scoreDiscount(){
        InstallmentEntity i = new InstallmentEntity();
        i.setRut_installment("19.999.999-9");
        i.setPayment_amount(375000);
        i.setInstallmentState(0);

        float score = 903.4f;
        // como el estudiante tiene un puntaje entre 900 y 950,
        // la cuota de 375.000 tendrá 5% de descuento
        // 375000 - 375000 * 0.05 = 356250
        float correct = (float) (375000 - 375000 * 0.05);

        float discount = installmentsCalculation.scoreDiscount(i, 903.4f);
        assertEquals(correct, discount);
    }
}
