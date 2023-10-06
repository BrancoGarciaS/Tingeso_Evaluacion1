package com.example.BrancoGarcia_Tingeso_Evaluacion1.services;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.ExamEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories.ExamRepository;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {
    @Autowired
    ExamRepository examRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    InstallmentService installmentService;

    @Autowired
    InstallmentsCalculation installmentsCalculation;

    public ArrayList<ExamEntity> getExams(){
        return (ArrayList<ExamEntity>) examRepository.findAll();
    }

    public ExamEntity saveDataDB(String rut, String date, String score){
        ExamEntity exam = new ExamEntity();
        exam.setRut(rut);
        exam.setExam_date(date);
        exam.setExam_score(Float.parseFloat(score));
        exam.setLoad_date(LocalDate.now());
        return examRepository.save(exam);
    }

    public void deleteAllExams(){
        examRepository.deleteAll();
    }

    @Generated
    public void readCsv(String filename){
        String texto = "";
        BufferedReader bf = null;
        //dataRepository.deleteAll();
        try{
            bf = new BufferedReader(new FileReader(filename));
            String temp = "";
            String bfRead;
            int count = 1;
            while((bfRead = bf.readLine()) != null){
                if (count == 1){
                    count = 0;
                }
                else{
                    saveDataDB(bfRead.split(";")[0],
                            bfRead.split(";")[1],
                            bfRead.split(";")[2]);
                    temp = temp + "\n" + bfRead;
                }
            }
            texto = temp;
            System.out.println("Archivo leido exitosamente");
        }catch(Exception e){
            System.err.println("No se encontro el archivo");
        }finally{
            if(bf != null){
                try{
                    bf.close();
                }catch(IOException e){
                    //logg.error("ERROR", e);
                }
            }
        }
    }

    @Generated
    public String saveFile(MultipartFile file){
        String filename = file.getOriginalFilename();
        if(filename != null){
            if(!file.isEmpty()){
                try{
                    byte [] bytes = file.getBytes();
                    Path path  = Paths.get(file.getOriginalFilename());
                    Files.write(path, bytes);
                    System.out.printf("archivo guardado");
                    //logg.info("Archivo guardado");
                }
                catch (IOException e){
                    //logg.info("ERROR", e);
                    System.out.printf("error");
                }
            }
            return "Archivo guardado con exito!";
        }
        else{
            return "No se pudo guardar el archivo";
        }
    }

    public List<Object[]> getMeanExams(){
        return examRepository.groupMean();
    }

    public List<Object[]> saveMean(){
        // agrupo por rut y calculo media
        List<Object[]> results = examRepository.groupMean();
        for (Object[] result : results) {
            // por cada fila de la consulta
            String rut = (String) result[0];
            Double mean = (Double) result[1];
            Long num_exams = (Long) result[2];
            // obtengo el estudiante por el rut
            Optional<StudentEntity> student_rut = studentService.getByRut(rut);
            if(student_rut.isPresent()){
                System.out.print("Rut encontrado:" + rut);
                StudentEntity s = student_rut.get();
                float meanFloat = mean.floatValue();
                s.setScore(meanFloat);
                Long n = num_exams + s.getNum_exams();
                s.setNum_exams(n);
                studentService.saveStudents(s);
                if(s.getPayment_type() == 1 || !(s.getAll_installments().isEmpty())){
                    ArrayList<InstallmentEntity> cuotas = installmentService.getInstallmentsByRut(rut);
                    for(InstallmentEntity cuota : cuotas){
                        if(cuota.getInstallmentState() == 0){ // si la cuota está pendiente
                            float m = installmentsCalculation.scoreDiscount(cuota, meanFloat);
                            cuota.setPayment_amount(m);
                            installmentService.saveData(cuota);
                            System.out.print("Descuento aplicado\n");
                        }
                    }
                }
            }
            else{
                System.out.print("Rut no encontrado:" + rut);
            }
        }
        return results;
    }

}
