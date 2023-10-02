package com.example.BrancoGarcia_Tingeso_Evaluacion1.services;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories.InstallmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstallmentService {
    @Autowired
    InstallmentRepository installmentRepository;

    public List<InstallmentEntity> getAll(){
        return (List<InstallmentEntity>) installmentRepository.findAll();
    }

    public InstallmentEntity saveData(InstallmentEntity cuota){
        return installmentRepository.save(cuota);
    }

    public ArrayList<InstallmentEntity> getInstallmentsByRut(String rut){
        ArrayList<InstallmentEntity> installments_list =
                installmentRepository.findInstallmentByRut(rut);
        return installments_list;
    }

    public Optional<InstallmentEntity> getInstallmentById(Long idInstallment) {
        // Implementa la lógica para recuperar una cuota por su ID desde la base de datos
        return installmentRepository.findById(idInstallment);
    }

    public void pay_installment(Optional<InstallmentEntity> installment) {
        InstallmentEntity installment_rut = installment.get();
        // Se realiza el procesamiento de pago aquí
        // Se actualiza el estado de la cuota a "pagado"
        installment_rut.setInstallmentState(1); // 1 para pagado
        // Establecer la fecha de pago actual, por ejemplo
        installment_rut.setPayment_date(LocalDate.now());
        // Guardar la cuota actualizada en la base de datos
        saveData(installment_rut);
    }

}
