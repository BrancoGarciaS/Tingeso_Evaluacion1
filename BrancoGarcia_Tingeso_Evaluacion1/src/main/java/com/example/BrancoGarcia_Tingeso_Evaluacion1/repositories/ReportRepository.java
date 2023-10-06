package com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.ReportEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends CrudRepository<ReportEntity, Long> {
    @Query("SELECT r FROM ReportEntity r WHERE r.rut = :rut")
    Optional<ReportEntity> findReportByRut(@Param("rut") String rut);
}
