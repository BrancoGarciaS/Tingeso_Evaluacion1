package com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.ExamEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends CrudRepository<ExamEntity, Long> {
    @Query("SELECT rut, AVG(exam_score), COUNT(*) FROM ExamEntity GROUP BY rut")
    List<Object[]> groupMean();
}
