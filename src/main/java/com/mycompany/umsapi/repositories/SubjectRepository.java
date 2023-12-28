package com.mycompany.umsapi.repositories;

import com.mycompany.umsapi.models.faculty.Department;
import com.mycompany.umsapi.models.faculty.Semester;
import com.mycompany.umsapi.models.faculty.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByDepartmentsContainingAndSemesterContaining(Department department, Semester semester);
    List<Subject> findAllByDepartmentsContaining(Department department);

    List<Subject> findAllByName(String name);

}
