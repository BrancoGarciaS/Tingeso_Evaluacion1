package com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallmentRepository extends CrudRepository<InstallmentEntity, Long> {

}
