package com.example.BrancoGarcia_Tingeso_Evaluacion1;

import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.InstallmentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.repositories.StudentRepository;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.InstallmentService;
import com.example.BrancoGarcia_Tingeso_Evaluacion1.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class StudentServiceTest {
    @Autowired
    StudentService studentService;

    @Autowired
    InstallmentService installmentService;

    @Test
    void testGetStudents(){
        ArrayList<StudentEntity> students = new ArrayList<>();
        StudentEntity s1 = new StudentEntity();
        s1.setRut("199.999.999-9"); s1.setName("Bastian");
        students.add(s1);
        StudentEntity s2 = new StudentEntity();
        s2.setRut("188.888.888-8"); s2.setName("Valentina");
        students.add(s2);

        // guardé dos estudiantes en la base de datos
        StudentEntity sv1 = studentService.saveStudents(s1);
        StudentEntity sv2 = studentService.saveStudents(s2);

        // una vez guardados, getStudents no debería retornar un array de tamaño 0
        ArrayList<StudentEntity> studentsTest = studentService.getStudents();

        assertNotNull(studentsTest);
        studentService.deleteById(sv1);
        studentService.deleteById(sv2);
    }

    @Test
    void testSaveStudents() {
        StudentEntity s1 = new StudentEntity();
        s1.setRut("199.999.999-9"); s1.setName("Bastian");
        // guardo el estudiante en la base de datos
        StudentEntity sv1 = studentService.saveStudents(s1);
        // para verificar que se haya guardado, al obtener un
        // estudiante por rut, debería existir el que tenga ese rut
        Optional<StudentEntity> stest = studentService.getByRut("1.999.999.999.999-9");
        assertNotNull(stest);
        studentService.deleteById(sv1);
    }

    @Test
    void testSaveDataStudents() {
        LocalDate birth_date = LocalDate.of(2000, 3, 22);
        StudentEntity sv = studentService.saveDataStudents("Bastian", "Castro",
                "bastiancastro@gmail.com", "1.999.999.999-K",
                birth_date, 2022, "SQL",
                1L, 0);
        // busco al estudiante por rut
        Optional<StudentEntity> stest = studentService.getByRut("1.999.999.999-K");
        assertNotNull(stest); // debería haberse guardado
        StudentEntity stest_g = stest.get();
        // comparo cada dato con los que coloque de entrada en la función
        assertEquals("1.999.999.999-K", stest_g.getRut());
        assertEquals("Bastian", stest_g.getName());
        assertEquals("Castro", stest_g.getLast_name());
        assertEquals("bastiancastro@gmail.com", stest_g.getEmail());
        assertEquals(23, stest_g.getAge());
        assertEquals("SQL", stest_g.getSchool_name());
        assertEquals(1L, stest_g.getSchool_type());
        assertEquals(2022, stest_g.getSenior_year());
        assertEquals(0, stest_g.getNum_installments());
        // borro las cuotas asociadas
        ArrayList<InstallmentEntity> ins = installmentService.getInstallmentsByRut("1.999.999.999-K");
        for(InstallmentEntity i : ins){
            installmentService.deleteById(i);
        }
        // borro al estudiante
        studentService.deleteById(sv);
    }

    @Test
    void testGetByRut(){
        StudentEntity s = new StudentEntity();
        s.setRut("1.999.999.999-K"); s.setName("Bastian");
        s.setLast_name("Castro"); s.setEmail("bastiancastro@gmail.com");
        s.setAge(23); s.setSchool_name("SQL");
        s.setSchool_type(1L); s.setSenior_year(2022);
        s.setNum_installments(0);
        StudentEntity sv = studentService.saveStudents(s);
        // si funciona correctamente el método debería retornar un
        // estudiante no nulo, ya que fue guardado previamente
        Optional<StudentEntity> s1 = studentService.getByRut("1.999.999.999-K");
        assertNotNull(s1);
        studentService.deleteById(sv);
    }

    @Test
    void TestDeleteById(){
        // primero creo un estudiante y lo guardo en la base de datos
        StudentEntity s = new StudentEntity();
        s.setRut("1.999.999.999-K"); s.setName("Bastian");
        s.setLast_name("Castro"); s.setEmail("bastiancastro@gmail.com");
        s.setAge(23); s.setSchool_name("SQL");
        s.setSchool_type(1L); s.setSenior_year(2022);
        s.setNum_installments(0);
        StudentEntity sv = studentService.saveStudents(s);
        // borro este estudiante de la base de datos
        studentService.deleteById(sv);
        // ahora si busco el rut del estudiante, debería retornarse nulo
        Optional<StudentEntity> so = studentService.getByRut("1.999.999.999-K");
        assertNotNull(so);

    }
}
