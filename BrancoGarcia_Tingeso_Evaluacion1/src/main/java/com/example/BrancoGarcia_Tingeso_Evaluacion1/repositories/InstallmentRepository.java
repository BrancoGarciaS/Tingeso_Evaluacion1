package com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface InstallmentRepository extends CrudRepository<InstallmentEntity, Long> {
    @Query("SELECT c FROM InstallmentEntity c WHERE c.rut_installment = :rut_installment")
    ArrayList<InstallmentEntity> findInstallmentByRut(@Param("rut_installment") String rut_installment);
}
