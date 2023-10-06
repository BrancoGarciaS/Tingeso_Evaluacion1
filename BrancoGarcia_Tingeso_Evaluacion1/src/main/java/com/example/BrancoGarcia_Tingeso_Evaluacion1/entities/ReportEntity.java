package com.example.BrancoGarcia_Tingeso_Evaluacion1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id_report; // atributo llave

    @Column(unique = true, nullable = false)
    private String rut; // rut del estudiante
    private String name_student; // nombre del estudiante
    private String last_name; // apellido del estudiante
    private Long num_exams; // numero de examenes rendidos
    private float mean_score; // promedio del puntaje de examenes
    private float total_tariff; // monto total de arancel a pagar
    private float interes_tariff; // tarifa con intereses
    private Integer payment_type; // tipo de pago
    private Integer num_installments; // numero total de cuotas pactadas
    private Integer num_installments_paid; // numero de cuotas pagadas
    private float tariff_paid; // monto total pagado
    private LocalDate last_payment; // fecha del ultimo pago
    private float tariff_to_pay; // tarifa a pagar
    private Integer late_installments; // numero de cuotas atrasadas

    public Long getId_report() {
        return id_report;
    }

    public void setId_report(Long id_report) {
        this.id_report = id_report;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getName_student() {
        return name_student;
    }

    public void setName_student(String name_student) {
        this.name_student = name_student;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Long getNum_exams() {
        return num_exams;
    }

    public void setNum_exams(Long num_exams) {
        this.num_exams = num_exams;
    }

    public float getMean_score() {
        return mean_score;
    }

    public void setMean_score(float mean_score) {
        this.mean_score = mean_score;
    }

    public float getTotal_tariff() {
        return total_tariff;
    }

    public void setTotal_tariff(float total_tariff) {
        this.total_tariff = total_tariff;
    }

    public float getInteres_tariff() {
        return interes_tariff;
    }

    public void setInteres_tariff(float interes_tariff) {
        this.interes_tariff = interes_tariff;
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

    public void setNum_installments(Integer num_installments) {
        this.num_installments = num_installments;
    }

    public Integer getNum_installments_paid() {
        return num_installments_paid;
    }

    public void setNum_installments_paid(Integer num_installments_paid) {
        this.num_installments_paid = num_installments_paid;
    }

    public float getTariff_paid() {
        return tariff_paid;
    }

    public void setTariff_paid(float tariff_paid) {
        this.tariff_paid = tariff_paid;
    }

    public LocalDate getLast_payment() {
        return last_payment;
    }

    public void setLast_payment(LocalDate last_payment) {
        this.last_payment = last_payment;
    }

    public float getTariff_to_pay() {
        return tariff_to_pay;
    }

    public void setTariff_to_pay(float tariff_to_pay) {
        this.tariff_to_pay = tariff_to_pay;
    }

    public Integer getLate_installments() {
        return late_installments;
    }

    public void setLate_installments(Integer late_installments) {
        this.late_installments = late_installments;
    }
}

