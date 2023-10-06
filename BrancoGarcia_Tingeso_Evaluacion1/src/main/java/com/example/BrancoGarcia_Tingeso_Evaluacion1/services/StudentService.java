package com.example.BrancoGarcia_Tingeso_Evaluacion1.services;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    InstallmentsCalculation installmentsCalculation;

    @Autowired
    InstallmentService installmentService;

    // para mostrar los datos de todos los estudiantes (get)
    public ArrayList<StudentEntity> getStudents(){
        return (ArrayList<StudentEntity>) studentRepository.findAll();
    }

    // para guardar a un estudiante(post)
    public StudentEntity saveStudents(StudentEntity usuario){
        return studentRepository.save(usuario);
    }

    // para guardar los datos de los estudiantes (post)
    public StudentEntity saveDataStudents(String name, String lastName, String email,
                                          String rut, LocalDate birth_date, int senior_year,
                                          String schoolName, Long schoolType, Integer installments){

        // primero compruebo si el estudiante está previamente registrado
        Optional<StudentEntity> student_rut = studentRepository.findByRut(rut);
        if(student_rut.isPresent()){
            // si el estudiante está presente en la base de datos
            return null;
        }
        StudentEntity student = new StudentEntity();

        // guardo la información en student
        student.setName(name);
        student.setLast_name(lastName);
        student.setEmail(email);
        student.setRut(rut);
        LocalDate now = LocalDate.now(); // fecha de ahora
        Period period = Period.between(birth_date, now); // obtengo periodo entre fecha de nacimiento y hoy
        int age = period.getYears();
        student.setAge(age);
        student.setSenior_year(senior_year);
        student.setSchool_name(schoolName);
        student.setSchool_type(schoolType);
        student.setNum_exams(0L); // al principio tendría 0 examenes rendidos

        if(installments > 0){
            student.setPayment_type(1); // pago en cuotas
        }
        if(installments == 0){
            student.setPayment_type(0); // pago al contado
        }
        // tipo de pago (iniciara por defecto como contado)
        if(schoolType == 2 && installments > 7){
            student.setNum_installments(7);
        }
        else if(schoolType == 3 && installments > 4){
            student.setNum_installments(4);
        }
        else{
            student.setNum_installments(installments); // iniciará con 0 cuotas pagas en total
        }

        student.setTuition(70000); // costo de la matrícula

        Integer studentTariff = installmentsCalculation.discount_tariff(student); // arancel con descuento
        student.setTariff(studentTariff); // valor del arancel con descuento
        student.setScore(-1); // promedio de examenes

        StudentEntity student_saved = saveStudents(student);
        if(student.getPayment_type() == 0){ // si pagó al contado
            List<InstallmentEntity> cuotas = new ArrayList<>();
            InstallmentEntity cuota = new InstallmentEntity();
            cuota.setInstallmentState(1); // se asume que el pago al contado se realiza inmediatamente
            cuota.setPayment_date(LocalDate.now());
            float pago_al_contado = 1500000 / 2;  // 50% del arancel
            cuota.setPayment_amount(pago_al_contado);
            cuota.setId_Student(student_saved); // le agrego el estudiante asociado
            cuota.setRut_installment(rut); // rut del estudiante que pagó al contado
            installmentService.saveData(cuota); // guardo el pago en la base de datos
            cuotas.add(cuota); // agrego el pago a la lista de cuotas del usuario
        }
        if(student.getPayment_type() == 1){ // si paga en cuotas
            List<InstallmentEntity> cuotas = new ArrayList<>();
            // divido el arancel con descuento entre el numero de cuotas
            float monto_por_cuota = (float) student.getTariff() / student.getNum_installments();
            for (int i = 0; i < student.getNum_installments(); i++) {
                // acá voy creando cada cuota en el momento
                InstallmentEntity cuota = new InstallmentEntity();
                // Calcula la fecha de vencimiento al dìa 10 de cada mes
                int year = LocalDate.now().getYear();
                int month = LocalDate.now().getMonthValue() + i + 1;
                // Ajustar el año y el mes por si se sobrepasan los 12 meses del año actual
                if(month > 12) {
                    year += (month - 1) / 12;
                    month = (month - 1) % 12 + 1;
                }
                // la fecha de vencimiento serìa el 10 de cada mes
                LocalDate dueDate = LocalDate.of(year, month, 10);
                // y la fecha de inicio de pago sería el 5 de cada mes
                LocalDate startDate = LocalDate.of(year, month, 5);
                cuota.setDue_date(dueDate);
                cuota.setStart_date(startDate);

                cuota.setInstallmentState(0); // 0 para pendiente (pago pendiente)
                cuota.setPayment_amount(monto_por_cuota);
                cuota.setId_Student(student_saved); // le agrego el estudiante asociado
                // cuota.setEstudiante(student_saved.getId_Student());
                cuota.setRut_installment(rut);
                installmentService.saveData(cuota); // guardo la cuota en la base de datos
                cuotas.add(cuota); // agrego la cuota a la lista de cuotas del usuario
            }
            student_saved.setAll_installments(cuotas);
        }
        return saveStudents(student_saved);

    }

    // para obtener estudiante por rut (get)
    public Optional<StudentEntity> getByRut(String rut){
        return studentRepository.findByRut(rut);
    }

    // para borrar un estudiante por id
    public String deleteById (StudentEntity student){
        try {
            studentRepository.deleteById(student.getId_Student());
            return "Usuario eliminado";
        } catch (Exception e) {
            return "No existe usuario con este ID";
        }
    }

}
