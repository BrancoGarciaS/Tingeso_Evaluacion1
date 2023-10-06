package com.example.BrancoGarcia_Tingeso_Evaluacion1.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity {
    // Atributos de la entidad
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id_Student; // atributo llave

    @OneToMany(mappedBy = "id_Student")
    @JsonIgnore
    private List<InstallmentEntity> all_installments; // el estudiante tiene una lista de cuotas

    @Column(unique = true, nullable = false)
    private String rut; // rut del estudiante

    private String name; // nombre del estudiante
    private String last_name; // apellido del estudiante
    private String email; // email del estudiante
    private Integer age; // año de nacimiento
    private String school_name; // nombre de la escuela
    private Long school_type; // tipo de escuela (1: municipal, 2: subvencionado, 3: privado)
    private Integer senior_year; // año de egreso de la escuela
    private Long num_exams; // numero de examenes realizados por el estudiante (inicializa en 0)
    private float score; // promedio del estudiante (se actualiza a través del excel de examenes)
    private Integer payment_type; // tipo de pago para el arancel(0: contado, 1: en cuotas)
    private Integer num_installments; // numero de cuotas (si es 0, significa que será pago al contado)
    private Integer tariff; // arancel total a pagar considerando descuentos
    private Integer tuition; // matrícula

    // setters y getters:

    public Long getId_Student() {
        return id_Student;
    }

    public void setId_Student(Long id_Student) {
        this.id_Student = id_Student;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public Long getSchool_type() {
        return school_type;
    }

    public void setSchool_type(Long school_type) {
        this.school_type = school_type;
    }

    public Integer getSenior_year() {
        return senior_year;
    }

    public void setSenior_year(Integer senior_year) {
        this.senior_year = senior_year;
    }

    public Long getNum_exams() {
        return num_exams;
    }

    public void setNum_exams(Long num_exams) {
        this.num_exams = num_exams;
    }

    public Integer getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(Integer payment_type) {
        this.payment_type = payment_type;
    }

    public Integer getNum_installments() {
        return num_installments;
    }

    public void setNum_installments(Integer installments) {
        this.num_installments = installments;
    }

    public Integer getTariff() {
        return tariff;
    }

    public void setTariff(Integer tariff) {
        this.tariff = tariff;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float exams_mean) {
        this.score = exams_mean;
    }

    public List<InstallmentEntity> getAll_installments() {
        return all_installments;
    }

    public void setAll_installments(List<InstallmentEntity> all_installments) {
        this.all_installments = all_installments;
    }

    public Integer getTuition() {
        return tuition;
    }

    public void setTuition(Integer tuition) {
        this.tuition = tuition;
    }
}
