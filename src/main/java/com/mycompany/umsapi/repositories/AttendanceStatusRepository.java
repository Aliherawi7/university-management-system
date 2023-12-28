package com.mycompany.umsapi.repositories;

import com.mycompany.umsapi.constants.AttendanceStatusName;
import com.mycompany.umsapi.models.attendance.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceStatusRepository extends JpaRepository<AttendanceStatus, Integer> {
    Optional<AttendanceStatus> findByAttendanceStatusName(AttendanceStatusName attendanceStatusName);
}
