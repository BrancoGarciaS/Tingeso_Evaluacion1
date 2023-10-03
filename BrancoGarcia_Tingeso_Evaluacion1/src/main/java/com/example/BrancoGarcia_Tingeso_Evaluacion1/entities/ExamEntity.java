package com.example.BrancoGarcia_Tingeso_Evaluacion1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id_exam; // atributo llave
    private String rut; // atributo que conecta con estudiante
    //private LocalDate exam_date; // fecha del examen
    private String exam_date;
    private float exam_score;
    private LocalDate load_date; // fecha en la que se cargó el examen

    public Long getId_exam() {
        return id_exam;
    }

    public void setId_exam(Long id_exam) {
        this.id_exam = id_exam;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getExam_date() {
        return exam_date;
    }

    public void setExam_date(String exam_date) {
        this.exam_date = exam_date;
    }

    public float getExam_score() {
        return exam_score;
    }

    public void setExam_score(float exam_score) {
        this.exam_score = exam_score;
    }

    public LocalDate getLoad_date() {
        return load_date;
    }

    public void setLoad_date(LocalDate load_date) {
        this.load_date = load_date;
    }
}
