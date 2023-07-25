package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Relative;
import com.mycompany.portalapi.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RelativeRepository extends JpaRepository<Relative, Long> {

    List<Relative> findAllByStudentId(Long StudentId);


}
