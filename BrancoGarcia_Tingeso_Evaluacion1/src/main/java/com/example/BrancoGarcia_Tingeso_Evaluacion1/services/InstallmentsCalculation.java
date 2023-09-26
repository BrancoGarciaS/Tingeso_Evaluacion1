package com.example.BrancoGarcia_Tingeso_Evaluacion1.services;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import org.springframework.stereotype.Service;

@Service
public class InstallmentsCalculation {
    public Integer schoolTypeDiscount(StudentEntity student){
        Integer tariff = 1500000;  // arancel total sin descuento
        Integer discount = 0; // descuento inicial
        if(student.getSchool_type() == 1){ // si su colegio es tipo municipal
            discount = (int) (tariff * 0.2); // hay descuento del 20%
        }
        else if(student.getSchool_type() == 2){ // si es su colegio es tipo subvencionado
            discount = (int) (tariff * 0.1); // hay descuento del 10%
        }
        // en caso de que el colegio sea privado (o sea 3) el descuento se conserva en 0
        return discount;
    }

    public Integer seniorYearDiscount(StudentEntity student){
        Integer tariff = 1500000;  // arancel total sin descuentos
        Integer discount = 0; // descuento inicial
        Integer seniorYear = 2023 - student.getSenior_year();
        if(seniorYear < 1){
            discount = (int) (tariff * 0.15); // hay descuento del 15%
        }
        // entre 1 o 2 años de egreso
        else if(seniorYear < 3){
            discount = (int) (tariff * 0.08); // hay descuento del 8%
        }
        //  entre los 3 años a 4 años de egreso
        else if(seniorYear < 5){
            discount = (int) (tariff * 0.04); // hay descuento del 4%
        }
        // a los 5 años o más no hay descuento (es igual a 0)
        return discount;
    }

    public Integer discount_tariff(StudentEntity student){
        Integer tariff = 1500000; // arancel total sin descuento
        // aplico cada uno de los descuentos
        tariff = tariff - schoolTypeDiscount(student);
        tariff = tariff - seniorYearDiscount(student);
        return tariff;
    }

    public Integer scoreDiscount(StudentEntity estudiante){
        Integer arancel = 1500000; // arancel total sin descuento
        Integer descuento = 0;
        Integer puntaje = estudiante.getExams_mean();
        if(puntaje >= 950 && puntaje <= 1000){
            descuento = (int) (arancel * 0.1);
        }
        else if(puntaje >= 900 && puntaje <= 949){
            descuento = (int) (arancel * 0.05);
        }
        else if(puntaje >= 850 && puntaje <= 899){
            descuento = (int) (arancel * 0.02);
        }
        // menos de 850 no tiene descuento
        return descuento;
    }
}
