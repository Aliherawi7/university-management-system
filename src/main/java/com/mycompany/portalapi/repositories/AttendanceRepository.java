package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.attendance.StudentAttendance;
import com.mycompany.portalapi.models.faculty.Semester;
import com.mycompany.portalapi.models.faculty.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<StudentAttendance, Long> {

    @Query("select sa from StudentAttendance sa where sa.student.id = :studentId" +
            " AND sa.date = :date" +
            " AND sa.subject.id = :subjectId" +
            " AND sa.semester.id = :semesterId")
    StudentAttendance findByStudentIdAndDateAndSubjectAndSemester(Long studentId, LocalDate date, Long subjectId, Long semesterId);
    @Query("select sa from StudentAttendance sa" +
            " where sa.student.id = :studentId" +
            " AND FUNCTION('YEAR', sa.date) = :year" +
            " AND FUNCTION('MONTH', sa.date) = :month" +
            " AND sa.semester.id = :semesterId" +
            " AND sa.subject.id = :subjectId")
    //studentId, year, month, semester,subject
    List<StudentAttendance> findAllByStudentIdAndDateAndSemesterAndSubject(Long studentId, Integer year, Integer month, Long semesterId, Long subjectId);
}
