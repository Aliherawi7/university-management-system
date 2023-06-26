package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByFieldOfStudyAndDepartment(String field, String department);
    List<Subject> findAllByFieldOfStudy(String field);

    List<Subject> findAllByName(String name);

}
