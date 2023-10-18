package com.example.BrancoGarcia_Tingeso_Evaluacion1;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.InstallmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InstallmentServiceTest {
    @Autowired
    InstallmentService installmentService;

    @Test
    void testGetAll(){
        InstallmentEntity i1 = new InstallmentEntity();
        i1.setRut_installment("1.999.999.999-9");
        i1.setInstallmentState(0);
        i1.setPayment_amount(250000f);
        InstallmentEntity i2 = new InstallmentEntity();
        i1.setRut_installment("1.999.999.999-9");
        i1.setInstallmentState(0);
        i1.setPayment_amount(250000f);

        // guardo cuotas en la base de datos
        InstallmentEntity i1s = installmentService.saveData(i1);
        InstallmentEntity i2s = installmentService.saveData(i2);

        // una vez guardadas, getInstallments no debería retornar un array de tamaño 0
        ArrayList<InstallmentEntity> insTest = (ArrayList<InstallmentEntity>) installmentService.getAll();
        assertNotNull(insTest);

        // borro las cuotas previamente guardadas
        installmentService.deleteById(i1s);
        installmentService.deleteById(i2s);
    }

    @Test
    void saveData(){
        InstallmentEntity i1 = new InstallmentEntity();
        i1.setRut_installment("1.999.999.999-9");
        i1.setInstallmentState(0);
        i1.setPayment_amount(250000f);
        // guardo la cuota en la base de datos
        InstallmentEntity sv1 = installmentService.saveData(i1);
        // para verificar que se haya guardado, al obtener la
        // cuota por id, debería retornarse la cuota
        Optional<InstallmentEntity> itest = installmentService.getInstallmentById(sv1.getId_installment());
        assertNotNull(itest);
        installmentService.deleteById(sv1);
    }

    @Test
    public void getInstallmentsByRut(){
        InstallmentEntity i1 = new InstallmentEntity();
        i1.setRut_installment("1.999.999.999-K");
        i1.setInstallmentState(0);
        i1.setPayment_amount(250000f);
        // guardo cuota en la base de datos
        InstallmentEntity sv = installmentService.saveData(i1);
        // si funciona correctamente el método debería retornar un
        // arraylist de cuotas no nulo, ya que fue guardado previamente
        ArrayList<InstallmentEntity> inst = installmentService.getInstallmentsByRut("1.999.999.999-K");
        assertNotEquals(0, inst.size());
        installmentService.deleteById(sv);
    }

    @Test
    void getInstallmentById() {
        InstallmentEntity i1 = new InstallmentEntity();
        i1.setRut_installment("1.999.999.999-K");
        i1.setInstallmentState(0);
        i1.setPayment_amount(250000f);
        // guardo cuota en la base de datos
        InstallmentEntity sv = installmentService.saveData(i1);
        // si funciona correctamente, el retorno de getInstallmentById
        // no debería ser nulo
        Optional<InstallmentEntity> ins = installmentService.getInstallmentById(sv.getId_installment());
        assertNotNull(ins);
        installmentService.deleteById(sv);
    }

    @Test
    void pay_installment() {
        InstallmentEntity i1 = new InstallmentEntity();
        i1.setRut_installment("1.999.999.999-K");
        i1.setInstallmentState(0);
        i1.setPayment_amount(250000f);
        // guardo cuota en la base de datos
        InstallmentEntity sv = installmentService.saveData(i1);
        // busco la cuota por id
        Optional<InstallmentEntity> ins = installmentService.getInstallmentById(sv.getId_installment());
        LocalDate actualDate = LocalDate.now();
        installmentService.pay_installment(ins);
        // Verificar si la fecha actual está entre el 5 y el 10 del mes
        if (actualDate.getDayOfMonth() >= 5 && actualDate.getDayOfMonth() <= 10) {
            // si está dentro del 5 y el 10, se debería
            // pagar la cuota, por lo que el estado seria 1
            Integer state = ins.get().getInstallmentState();
            assertEquals(1,state);
        }
        else{
            // si no está dentro del 5 y el 10, no se debería
            // pagar la cuota, por lo que el estado seria 0
            Integer state = ins.get().getInstallmentState();
            assertEquals(0,state);
        }
        installmentService.deleteById(sv);
    }

    @Test
    void apply_interest() {
        InstallmentEntity i1 = new InstallmentEntity();
        i1.setRut_installment("1.999.999.999-K");
        i1.setInstallmentState(0);
        i1.setPayment_amount(250000f);
        // la cuota se venció en agosto y han pasado dos meses
        LocalDate star_date = LocalDate.of(2023, 8, 5);
        LocalDate due_date = LocalDate.of(2023, 8, 10);
        i1.setStart_date(star_date);
        i1.setDue_date(due_date);
        // guardo cuota en la base de datos
        InstallmentEntity sv = installmentService.saveData(i1);
        LocalDate nowDate = LocalDate.now();
        // si estamos entre 5 y 10 no debería alterarse el monto de la cuota
        if(nowDate.getDayOfMonth() >= 5 && nowDate.getDayOfMonth() <= 10){
            assertEquals(250000f,sv.getPayment_amount());
        }
        installmentService.deleteById(sv);
    }

    @Test
    void interest(){
        installmentService.interest();
    }

    @Test
    void isLate(){
        InstallmentEntity i1 = new InstallmentEntity();
        i1.setRut_installment("1.999.999.999-K");
        i1.setInstallmentState(1);
        i1.setPayment_amount(250000f);
        LocalDate star_date = LocalDate.of(2023, 8, 5);
        LocalDate due_date = LocalDate.of(2023, 8, 10);
        // se pagó un mes despues asi que está atrasada
        LocalDate payment_date = LocalDate.of(2023, 9, 10);
        i1.setStart_date(star_date);
        i1.setDue_date(due_date);
        i1.setPayment_date(payment_date);
        // guardo cuota en la base de datos
        InstallmentEntity sv = installmentService.saveData(i1);

        boolean b = installmentService.isLate(sv);
        assertTrue(b); // debería estar atrasada

        installmentService.deleteById(sv);
    }

    @Test
    void deleteById(){
        // creo la cuota y la guardo en la base de datos
        InstallmentEntity i1 = new InstallmentEntity();
        i1.setRut_installment("1.999.999.999-K");
        i1.setInstallmentState(1);
        i1.setPayment_amount(250000f);
        InstallmentEntity sv = installmentService.saveData(i1);
        // borro la cuota guardada
        installmentService.deleteById(sv);
        // y busco por el id de la cuota, lo que debería retornar un valor nulo
        Optional<InstallmentEntity> ins = installmentService.getInstallmentById(sv.getId_installment());
        assertNotNull(ins);
    }
}
