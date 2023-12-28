package com.mycompany.umsapi.repositories;

import com.mycompany.umsapi.models.hrms.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByStudentId(Long studentId);

    Location findByStudentIdAndCurrent(Long studentId, boolean isCurrent);

}
