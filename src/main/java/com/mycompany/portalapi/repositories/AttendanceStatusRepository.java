package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.attendance.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceStatusRepository extends JpaRepository<AttendanceStatus, Integer> {
    Optional<AttendanceStatus> findByName(String name);
}
