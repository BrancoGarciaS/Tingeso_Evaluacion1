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

    // Para obtener todos los examenes
    public ArrayList<ExamEntity> getExams(){
        return (ArrayList<ExamEntity>) examRepository.findAll();
    }

    // Para guardar un examen en la base de datos
    public ExamEntity saveDataDB(String rut, String date, String score){
        ExamEntity exam = new ExamEntity();
        exam.setRut(rut);
        exam.setExam_date(date);
        exam.setExam_score(Float.parseFloat(score));
        exam.setLoad_date(LocalDate.now());
        return examRepository.save(exam);
    }

    // Para borrar los examenes (se usa cuando se calcula el promedio)
    public void deleteAllExams(){
        examRepository.deleteAll();
    }

    // Leer el archivo csv
    @Generated
    public void readCsv(String filename){
        String texto = ""; // para almacenar el contenido del texto
        BufferedReader bf = null; // Objeto para leer
        try{
            bf = new BufferedReader(new FileReader(filename)); // abro el archivo csv para lectura
            String temp = ""; // para acumular las líneas leidas
            String bfRead; // para almacenar cada línea de archivo
            int count = 1; // desde que fila va a leer (omite la primera línea del archivo csv que
            // tiene los nombres de las variables)
            while((bfRead = bf.readLine()) != null){ // mientras hayan lineas por leer
                if (count == 1){ // si el contador es 1
                    count = 0; // omite la primera fila del csv
                }
                else{ // sino, significa que se están leyendo las otras líneas
                    // guardo los datos de cada celda de la fila en la base de datos de examenes
                    saveDataDB(bfRead.split(";")[0],
                            bfRead.split(";")[1],
                            bfRead.split(";")[2]);
                    temp = temp + "\n" + bfRead; // acumulo la linea leida
                }
            }
            texto = temp;
            System.out.println("Archivo leido exitosamente");
        }catch(Exception e){
            System.err.println("No se encontro el archivo");
        }finally{
            if(bf != null){
                try{
                    bf.close(); // me encargo que se cierre BufferedReader
                }catch(IOException e){

                }
            }
        }
    }

    // Para guardar archivo csv
    @Generated
    public String saveFile(MultipartFile file){
        // obtengo el nombre del archivo
        String filename = file.getOriginalFilename();
        if(filename != null){ // si el nombre no es nulo y el archivo no está vacío
            if(!file.isEmpty()){
                try{
                    // Obtengo los bytes del archivo
                    byte [] bytes = file.getBytes();
                    // creo un archivo path del archivo
                    Path path  = Paths.get(file.getOriginalFilename());
                    // escribo los bytes en el archivo correspondiente
                    Files.write(path, bytes);
                    System.out.printf("archivo guardado");
                }
                catch (IOException e){ // en caso de error
                    System.out.printf("error");
                }
            }
            return "Archivo guardado con exito";
        }
        else{
            return "No se pudo guardar el archivo";
        }
    }

    // Para obtener la media de los examenes (posición 1: rut, posición 2: promedio,
    // posición 3: número de examenes)
    public List<Object[]> getMeanExams(){
        return examRepository.groupMean();
    }

    // Para guardar la media en la base de datos
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
            if(student_rut.isPresent()){ // si el estudiante está en la base de datos
                System.out.print("Rut encontrado:" + rut);
                StudentEntity s = student_rut.get();
                float meanFloat = mean.floatValue(); // como mean es double lo convierte a float
                s.setScore(meanFloat); // actualizo el promedio del estudiante
                Long n = num_exams + s.getNum_exams(); // sumo el número de examenes con el anterior
                s.setNum_exams(n);
                studentService.saveStudents(s);
                if(s.getPayment_type() == 1 || !(s.getAll_installments().isEmpty())){
                    // Para estudiantes con cuotas
                    ArrayList<InstallmentEntity> cuotas = installmentService.getInstallmentsByRut(rut);
                    for(InstallmentEntity cuota : cuotas){
                        // Por cada cuota pendiente
                        if(cuota.getInstallmentState() == 0){ // si la cuota está pendiente
                            // aplico descuento en base al promedio del estudiante
                            float m = installmentsCalculation.scoreDiscount(cuota, meanFloat);
                            // y modifico ese descuento en la cuota
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
