package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findAllByStudentIdAndYearNumberAndMonthNumberAndSemesterAndSubject(Long studentId, Integer year, Integer month, Integer semester, String subject);

    Attendance findByStudentIdAndYearNumberAndMonthNumberAndDayNumberAndSemesterAndSubject(Long studentId, Integer yearNumber, Integer monthNumber, Integer dayNumber, Integer semester, String subject);
}
