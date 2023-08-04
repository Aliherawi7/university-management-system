package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("select a from Attendance a where a.studentId = :studentId " +
            "and function('MONTH', a.localDate) = :month " +
            "and function('YEAR', a.localDate) = :year " +
            "And a.semester = :semester " +
            "And a.subject = :subject ")
    List<Attendance> getAttendanceByStudentIdAndYearAndMonthAndSubjectAndSemester(Long studentId, Integer year, Integer month,String subject, Integer semester);

    @Query("select a from Attendance a where a.studentId = :studentId " +
            "and function('MONTH', a.localDate) = :month " +
            "and function('YEAR', a.localDate) = :year " +
            "And a.semester = :semester " +
            "and a.subject = :subject " +
            "and a.fieldOfStudy = :fieldOfStudy " +
            "and a.department = :department")
    List<Attendance> getAttendanceYearAndMonthAndSubjectAndSemester
            (Integer year, Integer month, Integer semester,
             String subject, String fieldOfStudy, String department
            );


}
