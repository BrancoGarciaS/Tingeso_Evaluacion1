package com.example.BrancoGarcia_Tingeso_Evaluacion1.services;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    // para mostrar los datos de todos los estudiantes (get)
    public ArrayList<StudentEntity> getStudents(){
        return (ArrayList<StudentEntity>) studentRepository.findAll();
    }

    // para guardar los datos de los estudiantes (post)
    public StudentEntity saveStudents(StudentEntity usuario){
        return studentRepository.save(usuario);
    }

    //para obtener estudiante por id (get)
    public Optional<StudentEntity> getById(Long id){
        return studentRepository.findById(id);
    }

    // para obtener estudiante por rut (get)
    public Optional<StudentEntity> getByRut(String rut){
        return studentRepository.findByRut(rut);
    }

    // para borrar un estudiante por id
    public String deleteById (Long id){
        try {
            studentRepository.deleteById(id);
            return "Usuario eliminado";
        } catch (Exception e) {
            return "No existe usuario con este ID";
        }
    }
}
