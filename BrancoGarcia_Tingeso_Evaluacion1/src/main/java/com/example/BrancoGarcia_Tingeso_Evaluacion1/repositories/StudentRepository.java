package com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByRut(String rut);

}
