package com.mycompany.portalapi.models.attendance;


import com.mycompany.portalapi.models.faculty.Department;
import com.mycompany.portalapi.models.faculty.Semester;
import com.mycompany.portalapi.models.faculty.Subject;
import com.mycompany.portalapi.models.hrms.Staff;
import com.mycompany.portalapi.models.hrms.Student;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class StaffAttendance implements Comparable {
    @SequenceGenerator(sequenceName = "staff_attendance_sequence", name = "staff_attendance_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_attendance_sequence")
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
    @ManyToOne
    @JoinColumn(name = "attendance_status_id")
    private AttendanceStatus attendanceStatus;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Override
    public int compareTo(Object o) {
        StudentAttendance studentAttendance = (StudentAttendance) o;
        return this.date.compareTo(studentAttendance.getDate());
    }

}
