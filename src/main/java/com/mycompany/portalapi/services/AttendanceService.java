package com.mycompany.portalapi.services;


import com.mycompany.portalapi.dtos.*;
import com.mycompany.portalapi.models.Attendance;
import com.mycompany.portalapi.repositories.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StudentService studentService;

    public APIResponse addAttendance(Attendance attendance) {
        Attendance prevAttendance = attendanceRepository.findByStudentIdAndYearNumberAndMonthNumberAndDayNumberAndSemesterAndSubject(
                attendance.getStudentId(), attendance.getYearNumber(), attendance.getMonthNumber(),
                attendance.getDayNumber(), attendance.getSemester(), attendance.getSubject()
        );
        if (prevAttendance != null) {
            attendance.setIsPresent(attendance.getIsPresent());
            attendanceRepository.save((prevAttendance));
        } else {
            attendanceRepository.save(attendance);
        }
        return APIResponse.builder()
                .zonedDateTime(ZonedDateTime.now())
                .httpStatus(HttpStatus.CREATED)
                .message("اطلاعات محصل با موفقیت ثبت شد.")
                .statusCode(HttpStatus.CREATED.value())
                .build();
    }

    public List<Attendance> getStudentAttendanceBySubjectAndSemesterAndSubjectAndDate(
            Long studentId, String subjectId, Integer semester, Integer year, Integer month
    ) {
        List<Attendance> studentAttendances = attendanceRepository.findAllByStudentIdAndYearNumberAndMonthNumberAndSemesterAndSubject(
                studentId, year, month, semester, subjectId
        );

        return studentAttendances;
    }

    public AttendanceStudentListResponse getTheAttendanceList(
            String fieldOfStudy, String department, Integer semester,
            String subject, Integer year, Integer month
    ) {
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
        LocalDate localDate = LocalDate.of(year, month, 1);
        int daysWithoutHolidays = localDate.lengthOfMonth() - getFridays(localDate.getYear(), localDate.getMonthValue()).length;

        students.forEach(student -> {
            // get all attendances of the current student
            List<Attendance> studentAttendances = getStudentAttendanceBySubjectAndSemesterAndSubjectAndDate
                    (student.id(), subject, semester, year, month);
            List<MonthlyAttendance> monthlyAttendances = new ArrayList<>();
            LocalDate[] fridays = getFridays(localDate.getYear(), localDate.getMonthValue());
            for (int i = 1; i <= localDate.getMonth().length(localDate.isLeapYear()); i++) {

                int finalI = i;
                Optional<Attendance> monthlyAttendance = studentAttendances.stream().filter(item -> item.getDayNumber() == finalI).findFirst();
                boolean isPresent = false;
                if (monthlyAttendance.isPresent()) {
                    isPresent = true;
                }

                int currentDay = i;
                Optional<LocalDate> isFriday = Stream.of(fridays).filter(item -> item.getDayOfMonth() == currentDay).findFirst();
                monthlyAttendances.add(MonthlyAttendance.builder()
                        .day(i)
                        .isPresent(isPresent)
                        .isHoliday(isFriday.isPresent())
                        .month(localDate.getMonthValue())
                        .year(localDate.getYear())
                        .build()
                );
            }

            monthlyAttendances = monthlyAttendances.stream().sorted((o1, o2) -> o1.day().compareTo(o2.day())).toList();


            int totalPresentDays = (int) studentAttendances.stream().filter(Attendance::getIsPresent).count();
            int totalAbsentDays =  daysWithoutHolidays - totalPresentDays;

//            if(localDate.getMonthValue() == LocalDate.now().getMonthValue()) {
//                int counter = 0;
//                for(int i = 1; i <=LocalDate.now().getDayOfMonth(); i++){
//                    if(LocalDate.of())
//                }
//
//            }

            studentAttendanceResponses.add(
                    StudentAttendanceResponse.builder()
                            .monthlyAttendance(monthlyAttendances)
                            .totalPresent(totalPresentDays)
                            .totalAbsent(totalAbsentDays)
                            .name(student.name())
                            .fatherName(student.fatherName())
                            .studentId(student.id())
                            .build()
            );
        });
        return AttendanceStudentListResponse.builder()
                .students(studentAttendanceResponses)
                .daysInMonth(localDate.lengthOfMonth())
                .daysWithoutHolidays(daysWithoutHolidays)
                .monthDetails(getMonthDetails(localDate))
                .localDate(localDate)
                .fieldOfStudy(fieldOfStudy)
                .department(department)
                .subject(subject)
                .build();
    }

    public List<Attendance> sortAttendanceBaseOnDay(List<Attendance> attendances) {
        return attendances.stream().sorted().toList();
    }

    public static LocalDate[] getFridays(int year, int month) {
        List<LocalDate> fridayList = new ArrayList<>();

        LocalDate date = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());

        while (date.isBefore(lastDayOfMonth) || date.isEqual(lastDayOfMonth)) {
            if (date.getDayOfWeek() == DayOfWeek.FRIDAY) {
                fridayList.add(date);
            }
            date = date.plusDays(1);
        }

        LocalDate[] fridaysArray = new LocalDate[fridayList.size()];
        return fridayList.toArray(fridaysArray);
    }

    public List<DayDetails> getMonthDetails(LocalDate localDate) {
        List<DayDetails> monthDetails = new ArrayList<>();
        LocalDate[] fridays = getFridays(localDate.getYear(), localDate.getMonthValue());
        for (int i = 1; i <= localDate.getMonth().length(localDate.isLeapYear()); i++) {
            LocalDate date = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), i);
            boolean isHoliday = date.getDayOfWeek().equals(DayOfWeek.FRIDAY);
            monthDetails.add(DayDetails.builder()
                    .dayOfMonth(i)
                    .dayOfWeek(date.getDayOfWeek().name())
                    .isHoliday(isHoliday)
                    .build()
            );
        }
        return monthDetails;
    }
}
