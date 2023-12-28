package com.mycompany.umsapi.models.attendance;

import com.mycompany.umsapi.models.faculty.Department;
import com.mycompany.umsapi.models.faculty.Semester;
import com.mycompany.umsapi.models.faculty.Subject;
import com.mycompany.umsapi.models.hrms.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentAttendance implements Comparable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "attendance_status_id")
    private AttendanceStatus attendanceStatus;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Override
    public int compareTo(Object o) {
        StudentAttendance studentAttendance = (StudentAttendance) o;
        return this.date.compareTo(studentAttendance.date);
    }

}
