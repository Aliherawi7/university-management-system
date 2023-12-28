package com.mycompany.umsapi.models.attendance;


import com.mycompany.umsapi.models.faculty.Department;
import com.mycompany.umsapi.models.hrms.Staff;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class StaffAttendance implements Comparable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
