package com.example.BrancoGarcia_Tingeso_Evaluacion1.services;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories.InstallmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstallmentService {
    @Autowired
    InstallmentRepository installmentRepository;

    public List<InstallmentEntity> getAll(){
        return (List<InstallmentEntity>) installmentRepository.findAll();
    }

    public InstallmentEntity saveData(InstallmentEntity cuota){
        return installmentRepository.save(cuota);
    }

    public ArrayList<InstallmentEntity> getInstallmentsByRut(String rut){
        ArrayList<InstallmentEntity> installments_list =
                installmentRepository.findInstallmentByRut(rut);
        return installments_list;
    }

    public Optional<InstallmentEntity> getInstallmentById(Long idInstallment) {
        // Implementa la lógica para recuperar una cuota por su ID desde la base de datos
        return installmentRepository.findById(idInstallment);
    }

    public int pay_installment(Optional<InstallmentEntity> installment) {
        LocalDate actualDate = LocalDate.now();
        // Verificar si la fecha actual está entre el 5 y el 10 del mes
        if (actualDate.getDayOfMonth() >= 5 && actualDate.getDayOfMonth() <= 10) {
            InstallmentEntity installment_rut = installment.get();
            // Se realiza el procesamiento de pago aquí
            // Se actualiza el estado de la cuota a "pagado"
            installment_rut.setInstallmentState(1); // 1 para pagado
            // Establecer la fecha de pago actual, por ejemplo
            installment_rut.setPayment_date(LocalDate.now());
            // Guardar la cuota actualizada en la base de datos
            saveData(installment_rut);
            return  1; // pago con éxito
        }
        return 0; // pago rechazado
    }


    public void apply_interest(){
        LocalDate nowDate = LocalDate.now();
        // obtengo todas las cuotas
        ArrayList<InstallmentEntity> i = (ArrayList<InstallmentEntity>) installmentRepository.findAll();
        // para aplicar el interes fuera del rango de pago (5 y 10 de cada mes)
        if(!(nowDate.getDayOfMonth() >= 5 && nowDate.getDayOfMonth() <= 10)){
            if(i.size() > 1){ // si hay cuotas y no se pagó al contado
                InstallmentEntity installment = i.get(0); // saco la primera cuota
                if(installment.getInterest_date() == null){ // si no se ha calculado interes nunca
                    interest(); // aplico intereses
                    for(InstallmentEntity ins : i){
                        ins.setInterest_date(LocalDate.now()); // le aplico la hora actual a cada cuota
                        installmentRepository.save(ins); // guardo cada cuota con la hora de interés
                    }
                    return;
                }
                // solo se puede calcular el interes de las cuotas una vez por mes
                else if(nowDate.getMonthValue() != installment.getInterest_date().getMonthValue()){
                    // si se ha calculado interes antes en el mismo mes no se hace nada,
                    // si se aplicó interes un mes distinto al pasado, se calcula nuevamente
                    interest();
                    for(InstallmentEntity ins : i){
                        ins.setInterest_date(LocalDate.now()); // le aplico la hora actual a cada cuota
                        installmentRepository.save(ins); // guardo cada cuota con la hora de interés
                    }
                }
            }
        }
    }

    public void interest(){
        LocalDate nowDate = LocalDate.now();
        ArrayList<InstallmentEntity> i = (ArrayList<InstallmentEntity>) installmentRepository.findAll();
        for(InstallmentEntity ins : i){ // por cada cuota
            if(ins.getInstallmentState() == 0){ // si la cuota está sin pagar
                if(nowDate.isAfter(ins.getDue_date())){ // si la fecha máxima de pago ya pasó
                    // calculo los meses entre la fecha de vencimiento y la actual
                    long monthsLate = ChronoUnit.MONTHS.between(ins.getDue_date(), nowDate);
                    float interest = 0;
                    if (monthsLate == 1) {
                        interest = 0.03F; // 3% de interés para 1 mes de atraso
                    }
                    else if (monthsLate == 2) {
                        interest = 0.06F; // 6% de interés para 2 meses de atraso
                    }
                    else if (monthsLate == 3) {
                        interest = 0.09F; // 9% de interés para 3 meses de atraso
                    }
                    else if (monthsLate > 3) {
                        interest = 0.15F; // 15% de interés para más de 3 meses de atraso
                    }
                    float original_amount = ins.getPayment_amount();
                    float interest_amount = original_amount + original_amount * interest;
                    ins.setPayment_amount(interest_amount);
                    installmentRepository.save(ins);
                }
            }
        }
    }

    public boolean isLate(InstallmentEntity inst){
        // en el caso que se haya pagado la cuota
        LocalDate due_date = inst.getDue_date();
        if(inst.getInstallmentState() == 1){
            LocalDate pay_date = inst.getPayment_date();
            // si la fecha de vencimiento de la cuota
            // es anterior a la fecha de pago, está atrasada
            if(due_date.isBefore(pay_date)){
                return true;
            }
        }
        // en caso que la cuota aun no haya sido pagada
        // y actualmente se ha sobrepasado la fecha de vencimiento
        else{
            // si la cuota tiene una fecha de vencimiento
            // que es anterior a la fecha actual
            if(due_date.isBefore(LocalDate.now())){
                return true;
            }
        }
        return false;
    }

    public String deleteById (InstallmentEntity installment){
        try {
            installmentRepository.deleteById(installment.getId_installment());
            return "Cuota eliminada";
        } catch (Exception e) {
            return "No existe cuota con este ID";
        }
    }
}
