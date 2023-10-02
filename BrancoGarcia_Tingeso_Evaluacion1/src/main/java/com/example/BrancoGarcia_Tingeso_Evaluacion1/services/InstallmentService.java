package com.example.BrancoGarcia_Tingeso_Evaluacion1.services;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories.InstallmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

}
