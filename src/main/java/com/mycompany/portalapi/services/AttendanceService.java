package com.mycompany.portalapi.services;


import com.mycompany.portalapi.constants.AttendanceStatusName;
import com.mycompany.portalapi.dtos.*;
import com.mycompany.portalapi.exceptions.IllegalArgumentException;
import com.mycompany.portalapi.models.Attendance;
import com.mycompany.portalapi.repositories.AttendanceRepository;
import com.mycompany.portalapi.repositories.AttendanceStatusRepository;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StudentService studentService;
    private final AttendanceStatusRepository attendanceStatusRepository;

    public APIResponse addAttendance(AttendanceDTO attendanceDTO) {
        Attendance prevAttendance = attendanceRepository.findByStudentIdAndYearNumberAndMonthNumberAndDayNumberAndSemesterAndSubject(
                attendanceDTO.studentId(), attendanceDTO.yearNumber(), attendanceDTO.monthNumber(),
                attendanceDTO.dayNumber(), attendanceDTO.semester(), attendanceDTO.subject()
        );

        if (prevAttendance != null) {
            prevAttendance.setAttendanceStatus(attendanceStatusRepository.findByName(attendanceDTO.attendanceStatus()).orElseThrow(() -> new IllegalArgumentException("حالت حاضری نامعتبر")));
//            log.info("prev attendance: id:{}, stdId:{}, isp:{}",prevAttendance.getId(),prevAttendance.getStudentId(),prevAttendance.getAttendanceStatus());
//            log.info("att after save. id:{}, isP:{} ",prevAttendance.getId(), attendanceRepository.findById(prevAttendance.getId()).get().getAttendanceStatus());
            attendanceRepository.save(prevAttendance);
            attendanceRepository.findAll().forEach(item -> {
                log.info("attlist: id{}, stId:{}, isP:{} ",item.getId(), item.getStudentId(), item.getAttendanceStatus());
            });
        } else {
           //log.info("curr attendance: id:{}, stdId:{}, isp:{}",attendance.getId(),attendance.getStudentId(),attendance.getAttendanceStatus());
            attendanceRepository.save(
                    Attendance.builder()
                            .attendanceStatus(attendanceStatusRepository.findByName(attendanceDTO.attendanceStatus()).orElseThrow(() -> new IllegalArgumentException("حالت حاضری نامعتبر است")))
                            .dayNumber(attendanceDTO.dayNumber())
                            .monthNumber(attendanceDTO.monthNumber())
                            .subject(attendanceDTO.subject())
                            .yearNumber(attendanceDTO.yearNumber())
                            .department(attendanceDTO.department())
                            .fieldOfStudy(attendanceDTO.fieldOfStudy())
                            .studentId(attendanceDTO.studentId())
                            .semester(attendanceDTO.semester())
                            .build()
            );
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
        log.info("studentAttendanceLIst:{}", studentAttendances.stream().map(item ->"[" +item.getStudentId() + " " + item.getAttendanceStatus() + "], ").collect(Collectors.joining("")));

        return studentAttendances;
    }

    public AttendanceStudentListResponse getTheAttendanceList(
            String fieldOfStudy, String department, Integer semester,
            String subject, Integer year, Integer month
    ) {
        /*
        * get all students with the specific fieldOfStudy, semester, and department
        * */
        Page<StudentShortInfo> students = studentService.getAllStudentsByRequestParams(
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
                AttendanceStatusName attendanceStatusName = AttendanceStatusName.UNKNOWN;
                if (monthlyAttendance.isPresent()) {
                    attendanceStatusName = AttendanceStatusName.valueOf(monthlyAttendance.get().getAttendanceStatus().getName());
                }

                int currentDay = i;
                Optional<LocalDate> isFriday = Stream.of(fridays).filter(item -> item.getDayOfMonth() == currentDay).findFirst();
                monthlyAttendances.add(MonthlyAttendance.builder()
                        .day(i)
                        .attendanceStatusName(attendanceStatusName)
                        .isHoliday(isFriday.isPresent())
                        .month(localDate.getMonthValue())
                        .year(localDate.getYear())
                        .build()
                );
            }

            monthlyAttendances = monthlyAttendances.stream().sorted((o1, o2) -> o1.day().compareTo(o2.day())).toList();


            int totalPresentDays = (int) studentAttendances.stream().filter(attendance -> attendance.getAttendanceStatus().getName().equals(AttendanceStatusName.PRESENT.getValue())).count();
            int totalAbsentDays = (int) studentAttendances.stream().filter(attendance -> attendance.getAttendanceStatus().getName().equals(AttendanceStatusName.ABSENT.getValue())).count();
            //deprecated
            //int totalAbsentDays =  daysWithoutHolidays - totalPresentDays;

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
