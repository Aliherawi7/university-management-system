package com.mycompany.umsapi.repositories;

import com.mycompany.umsapi.models.attendance.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<StudentAttendance, Long> {

}
