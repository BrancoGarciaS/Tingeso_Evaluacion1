package com.example.BrancoGarcia_Tingeso_Evaluacion1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Integer installmentState; // estado de la cuota, 0 pendiente, 1 pagado
    private float payment_amount;  // pago involucrado
    private Date payment_date;  // fecha de pago

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

    public Date getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }

    public StudentEntity getId_Student() {
        return id_Student;
    }

    public void setId_Student(StudentEntity id_Student) {
        this.id_Student = id_Student;
    }
}
