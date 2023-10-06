package com.example.BrancoGarcia_Tingeso_Evaluacion1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "installments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id_installment; // atributo llave
    private String rut_installment;
    private Integer installmentState; // estado de la cuota, 0 pendiente, 1 pagado
    private float payment_amount;  // pago involucrado
    private LocalDate due_date; // fecha de vencimiento
    private LocalDate start_date;  // fecha de inicio
    private LocalDate payment_date;  // fecha de pago
    private LocalDate interest_date; // fecha donde se aplicó el interés

    @ManyToOne
    @JoinColumn(name = "id_Student")
    private StudentEntity id_Student;  // conexión con entidad estudiante

    public Long getId_installment() {
        return id_installment;
    }

    public void setId_installment(Long id_installment) {
        this.id_installment = id_installment;
    }

    public Integer getInstallmentState() {
        return installmentState;
    }

    public void setInstallmentState(Integer installmentState) {
        this.installmentState = installmentState;
    }

    public float getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(float payment_amount) {
        this.payment_amount = payment_amount;
    }

    public LocalDate getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(LocalDate payment_date) {
        this.payment_date = payment_date;
    }

    public StudentEntity getId_Student() {
        return id_Student;
    }

    public void setId_Student(StudentEntity id_Student) {
        this.id_Student = id_Student;
    }

    public String getRut_installment() {
        return rut_installment;
    }

    public void setRut_installment(String rut_installment) {
        this.rut_installment = rut_installment;
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getInterest_date() {
        return interest_date;
    }

    public void setInterest_date(LocalDate interest_date) {
        this.interest_date = interest_date;
    }
}
