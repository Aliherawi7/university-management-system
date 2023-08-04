package com.mycompany.portalapi.services;


import com.mycompany.portalapi.dtos.*;
import com.mycompany.portalapi.models.Attendance;
import com.mycompany.portalapi.repositories.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StudentService studentService;

    public APIResponse addAttendance(Attendance attendance){

        attendanceRepository.save(attendance);
        return APIResponse.builder()
                .zonedDateTime(ZonedDateTime.now())
                .httpStatus(HttpStatus.CREATED)
                .message("اطلاعات محصل با موفقیت ثبت شد.")
                .statusCode(HttpStatus.CREATED.value())
                .build();
    }

    public List<Attendance> getStudentAttendanceBySubjectAndSemesterAndSubjectAndDate(
            Long studentId, String subjectId, Integer semester, Integer year, Integer month
    ){
        List<Attendance> studentAttendances = attendanceRepository.getAttendanceByStudentIdAndYearAndMonthAndSubjectAndSemester(
                studentId, year, month, subjectId, semester
        );
        return studentAttendances;
    }
    public AttendanceStudentListResponse getTheAttendanceList(
            String fieldOfStudy, String department, Integer semester,
            String subject, Integer year, Integer month
    ){
        // get all the students from db
        Page<StudentShortInfo> students = studentService.getAllPostsByRequestParams(
                null, fieldOfStudy, department, semester, 0, 300
        );

        /*
        * get each student's attendances in the provided month and then store them into
        * an object of StudentAttendanceResponse
        * and then put the studentAttendanceResponse objects into AttendanceStudentListResponse Object
        * */
        List<StudentAttendanceResponse> studentAttendanceResponses = new ArrayList<>();

        students.forEach(student -> {
            List<MonthlyAttendance> studentAttendances = getStudentAttendanceBySubjectAndSemesterAndSubjectAndDate
                    (student.id(), subject, semester, year, month)
                    .stream().map(studentAttendance -> {
                        return MonthlyAttendance.builder()
                                .day(studentAttendance.getLocalDate().getDayOfMonth())
                                .isPresent(studentAttendance.isPresent())
                                .month(studentAttendance.getLocalDate().getMonthValue())
                                .year(studentAttendance.getLocalDate().getYear())
                                .build();
                    }).toList();
            int totalPresentDays =(int) studentAttendances.stream().filter(MonthlyAttendance::isPresent).count();
            int totalAbsentDays = (int) (studentAttendances.size() - totalPresentDays);
            studentAttendanceResponses.add(
                    StudentAttendanceResponse.builder()
                    .monthlyAttendance(studentAttendances)
                    .totalPresent(totalPresentDays)
                    .totalAbsent(totalAbsentDays)
                    .name(student.name())
                    .fatherName(student.fatherName())
                    .studentId(student.id())
                    .build()
            );
        });
        LocalDate localDate = LocalDate.of(year, month,1);
        return AttendanceStudentListResponse.builder()
                .students(studentAttendanceResponses)
                .daysInMonth(localDate.lengthOfMonth())
                .localDate(localDate)
                .build();
    }

    public List<Attendance> sortAttendanceBaseOnDay(List<Attendance> attendances){
       return  attendances.stream().sorted().toList();
    }
}
