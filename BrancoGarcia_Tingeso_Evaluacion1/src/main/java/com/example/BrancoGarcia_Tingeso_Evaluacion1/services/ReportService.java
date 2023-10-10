package com.example.BrancoGarcia_Tingeso_Evaluacion1.services;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.ReportEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    StudentService studentService;
    @Autowired
    InstallmentService installmentService;

    public ReportEntity createReport(String rut){
        // actualizar cuotas
        installmentService.apply_interest();
        ReportEntity report = new ReportEntity();
        Optional<StudentEntity> s = studentService.getByRut(rut);
        if(s.isPresent()){ // si existe el estudiante en la base de datos
            // esta variable sirve en el caso de que se haya creado un reporte previamente
            // con ese rut
            Optional<ReportEntity> report_rut = reportRepository.findReportByRut(rut);
            StudentEntity student_rut = s.get();
            if(report_rut.isPresent() && student_rut.getPayment_type() == 1){ // si ya se habia creado un reporte con ese rut, se actualiza
                return updateReport(report_rut.get(), student_rut);
            }
            ArrayList<InstallmentEntity> c = installmentService.getInstallmentsByRut(rut);
            report.setRut(student_rut.getRut());
            report.setName_student(student_rut.getName());
            report.setLast_name(student_rut.getLast_name());
            report.setNum_exams(student_rut.getNum_exams());
            report.setMean_score(student_rut.getScore());
            report.setTotal_tariff(student_rut.getTariff());
            report.setPayment_type(student_rut.getPayment_type());
            if(student_rut.getPayment_type() == 0){
                // si pagó al contado
                report.setNum_installments(1);
                report.setInteres_tariff(student_rut.getTariff()); // no hay intereses
                report.setNum_installments_paid(1);
                if(c.size() > 0){
                    LocalDate d = c.get(0).getPayment_date();
                    report.setLast_payment(d); // la ultima fecha de pago y es unica
                }
                report.setNum_installments_paid(1); // se ha pagado una cuota
                report.setTariff_paid(0); // tarifa a pagar
                report.setLate_installments(0); // 0 cuotas atrasadas
                return reportRepository.save(report);
            }
            report.setNum_installments(student_rut.getNum_installments());
            int count_paid = 0; // numero de cuotas pagadas
            float tariff_paid_s = 0; // monto total pagado
            float tariff_to_pay = 0; // monto a pagar
            float interes_tariff = 0; // arancel a pagar con intereses
            Integer later = 0; // cuotas atrasadas
            if(c.size() > 0){
                LocalDate last_payment = c.get(0).getPayment_date();
                for(InstallmentEntity installment_s : c){
                    // si la cuota está pagada se aumenta el contador
                    if(installment_s.getInstallmentState() == 1){
                        count_paid++;
                        tariff_paid_s += installment_s.getPayment_amount();
                        // si aun no se ha encontrado una fecha de pago
                        // o si la fecha de pago de la cuota es más reciente que la fecha
                        // más reciente encontrada

                        if(last_payment != null && installment_s.getPayment_date() != null){
                            if(installment_s.getPayment_date().isAfter(last_payment)){
                                last_payment = installment_s.getPayment_date();
                            }
                        }
                        // en caso que aun no se ha encontrado una fecha de pago
                        else if(last_payment == null && installment_s.getPayment_date() != null){
                            last_payment = installment_s.getPayment_date();
                        }
                    }
                    else{ // si no está pagada
                        tariff_to_pay += installment_s.getPayment_amount();
                    }
                    interes_tariff += installment_s.getPayment_amount();
                    if(installmentService.isLate(installment_s)){
                        // si la cuota está atrasada se cuenta en atrasos
                        later++;
                    }
                }
                if (last_payment != null) { // Verifica si last_payment no es nulo
                    report.setLast_payment(last_payment);
                }
            }

            report.setInteres_tariff(interes_tariff);
            report.setNum_installments_paid(count_paid);
            report.setTariff_paid(tariff_paid_s);

            report.setTariff_to_pay(tariff_to_pay);
            report.setLate_installments(later);
            return reportRepository.save(report);
        }
        return null;
    }

    public ReportEntity updateReport(ReportEntity report, StudentEntity student_rut){
        String rut = report.getRut();
        ArrayList<InstallmentEntity> c = installmentService.getInstallmentsByRut(rut);
        report.setNum_exams(student_rut.getNum_exams());
        report.setMean_score(student_rut.getScore());

        int count_paid = 0; // numero de cuotas pagadas
        float tariff_paid_s = 0; // monto total pagado
        float tariff_to_pay = 0; // monto a pagar
        float interes_tariff = 0; // arancel a pagar con intereses
        Integer later = 0; // cuotas atrasadas
        if(c.size() > 0){
            LocalDate last_payment = c.get(0).getPayment_date();
            for(InstallmentEntity installment_s : c){
                // si la cuota está pagada se aumenta el contador
                if(installment_s.getInstallmentState() == 1){
                    count_paid++;
                    tariff_paid_s += installment_s.getPayment_amount();
                    // si aun no se ha encontrado una fecha de pago
                    // o si la fecha de pago de la cuota es más reciente que la fecha
                    // más reciente encontrada

                    if(last_payment != null && installment_s.getPayment_date() != null){
                        if(installment_s.getPayment_date().isAfter(last_payment)){
                            last_payment = installment_s.getPayment_date();
                        }
                    }
                    // en caso que aun no se ha encontrado una fecha de pago
                    else if(last_payment == null && installment_s.getPayment_date() != null){
                        last_payment = installment_s.getPayment_date();
                    }
                }
                else{ // si no está pagada
                    tariff_to_pay += installment_s.getPayment_amount();
                }
                interes_tariff += installment_s.getPayment_amount();
                if(installmentService.isLate(installment_s)){
                    // si la cuota está atrasada se cuenta en atrasos
                    later++;
                    System.out.println("atrasada: " + installment_s.getId_installment() + "  ");
                }
            }
            if (last_payment != null) { // Verifica si last_payment no es nulo
                report.setLast_payment(last_payment);
            }
        }

        report.setInteres_tariff(interes_tariff);
        report.setNum_installments_paid(count_paid);
        report.setTariff_paid(tariff_paid_s);

        report.setTariff_to_pay(tariff_to_pay);
        report.setLate_installments(later);
        return reportRepository.save(report);

    }
}
