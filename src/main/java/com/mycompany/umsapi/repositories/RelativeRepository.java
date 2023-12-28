package com.mycompany.umsapi.repositories;

import com.mycompany.umsapi.models.hrms.Relative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelativeRepository extends JpaRepository<Relative, Long> {

    List<Relative> findAllByStudentId(Long StudentId);


}
