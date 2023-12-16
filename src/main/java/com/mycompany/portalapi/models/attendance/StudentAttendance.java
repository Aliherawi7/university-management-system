package com.mycompany.portalapi.models.attendance;

import com.mycompany.portalapi.models.faculty.Department;
import com.mycompany.portalapi.models.faculty.Faculty;
import com.mycompany.portalapi.models.faculty.Semester;
import com.mycompany.portalapi.models.faculty.Subject;
import com.mycompany.portalapi.models.hrms.Student;
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
    @SequenceGenerator(sequenceName = "student_attendance_sequence", name = "student_attendance_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_attendance_sequence")
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
