package com.mycompany.umsapi.services;


import com.mycompany.umsapi.constants.AttendanceStatusName;
import com.mycompany.umsapi.dtos.*;
import com.mycompany.umsapi.dtos.attendanceDto.AttendanceDTO;
import com.mycompany.umsapi.dtos.attendanceDto.StudentAttendanceListResponse;
import com.mycompany.umsapi.dtos.attendanceDto.DayDetails;
import com.mycompany.umsapi.dtos.attendanceDto.MonthlyAttendance;
import com.mycompany.umsapi.dtos.studentDto.StudentAttendanceResponse;
import com.mycompany.umsapi.dtos.studentDto.StudentShortInfo;
import com.mycompany.umsapi.exceptions.IllegalArgumentException;
import com.mycompany.umsapi.exceptions.ResourceNotFoundException;
import com.mycompany.umsapi.models.attendance.StudentAttendance;
import com.mycompany.umsapi.models.faculty.Department;
import com.mycompany.umsapi.models.faculty.Faculty;
import com.mycompany.umsapi.models.faculty.Semester;
import com.mycompany.umsapi.models.faculty.Subject;
import com.mycompany.umsapi.models.hrms.Student;
import com.mycompany.umsapi.repositories.AttendanceRepository;
import com.mycompany.umsapi.repositories.AttendanceStatusRepository;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StudentService studentService;
    private final AttendanceStatusRepository attendanceStatusRepository;
    private final SubjectService subjectService;
    private final DepartmentService departmentService;
    private final FacultyService facultyService;

    public APIResponse addAttendance(AttendanceDTO attendanceDTO) {
        Student student = studentService.getStudentById(attendanceDTO.studentId());
        Faculty faculty = facultyService.getById(attendanceDTO.fieldOfStudy());

        Department department = faculty.getDepartments()
                .stream()
                .filter(d -> Objects.equals(d.getId(), attendanceDTO.department()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("invalid department id"));

        Subject subject = department.getSubjects()
                .stream()
                .filter(s -> Objects.equals(s.getId(), attendanceDTO.subject()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("invalid subject id"));
        Semester semester = department.getSemesters().stream()
                .filter(item -> Objects.equals(item.getId(), attendanceDTO.semesterId()))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("invalid semester id"));

        StudentAttendance prevStudentAttendance = (StudentAttendance) attendanceRepository.findAll();


        if (prevStudentAttendance != null) {
            prevStudentAttendance.setAttendanceStatus(attendanceStatusRepository
                    .findById(attendanceDTO.attendanceStatusId()).orElseThrow(() -> new IllegalArgumentException("Invalid attendance status id!")));
//            log.info("prev attendance: id:{}, stdId:{}, isp:{}",prevAttendance.getId(),prevAttendance.getStudentId(),prevAttendance.getAttendanceStatus());
//            log.info("att after save. id:{}, isP:{} ",prevAttendance.getId(), attendanceRepository.findById(prevAttendance.getId()).get().getAttendanceStatus());
            attendanceRepository.save(prevStudentAttendance);
            attendanceRepository.findAll().forEach(item -> {
                log.info("attlist: id{}, stId:{}, isP:{} ",item.getId(), item.getStudent().getId(), item.getAttendanceStatus());
            });
        } else {
           //log.info("curr attendance: id:{}, stdId:{}, isp:{}",attendance.getId(),attendance.getStudentId(),attendance.getAttendanceStatus());
            attendanceRepository.save(
                    StudentAttendance.builder()
                            .attendanceStatus(attendanceStatusRepository.findById(attendanceDTO.attendanceStatusId())
                                    .orElseThrow(() -> new IllegalArgumentException("Invalid attendance status id!")))
                            .subject(subject)
                            .department(department)
                            .student(student)
                            .semester(semester)
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



    public StudentAttendanceListResponse getTheAttendanceList(
            Long faculty, Long department, Long semester,
            Long subject, Integer year, Integer month
    ) {
        /*
        * get all students with the specific fieldOfStudy, semester, and department
        * */
        Page<StudentShortInfo> students = studentService.getAllStudentsByRequestParams(
                null, faculty, department, semester, 0, 300
        );

        /*
         * get each student's attendances in the provided month and then put them into
         * an object of StudentAttendanceResponse
         * and then put the studentAttendanceResponse objects into AttendanceStudentListResponse Object
         * */
        List<StudentAttendanceResponse> studentAttendanceResponses = new ArrayList<>();
        LocalDate localDate = LocalDate.of(year, month, 1);
        int daysWithoutHolidays = localDate.lengthOfMonth() - getFridays(localDate.getYear(), localDate.getMonthValue()).length;

        students.forEach(student -> {
            // get all attendances of the current student
            List<StudentAttendance> studentStudentAttendances = new ArrayList<>();
//                    getStudentAttendanceBySubjectAndSemesterAndSubjectAndDate
//                    (student.id(), subject, semester, year, month);
            List<MonthlyAttendance> monthlyAttendances = new ArrayList<>();
            LocalDate[] fridays = getFridays(localDate.getYear(), localDate.getMonthValue());

            for (int i = 1; i <= localDate.getMonth().length(localDate.isLeapYear()); i++) {
                int finalI = i;
                Optional<StudentAttendance> monthlyAttendance = studentStudentAttendances.stream().filter(item -> item.getDate().getDayOfMonth() == finalI).findFirst();
                AttendanceStatusName attendanceStatusName = AttendanceStatusName.UNKNOWN;
                if (monthlyAttendance.isPresent()) {
                    attendanceStatusName = AttendanceStatusName.valueOf(monthlyAttendance.get().getAttendanceStatus().getAttendanceStatusName().name());
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


            int totalPresentDays = (int) studentStudentAttendances.stream().filter(studentAttendance -> studentAttendance.getAttendanceStatus().getAttendanceStatusName().getValue().equals(AttendanceStatusName.PRESENT.getValue())).count();
            int totalAbsentDays = (int) studentStudentAttendances.stream().filter(studentAttendance -> studentAttendance.getAttendanceStatus().getAttendanceStatusName().getValue().equals(AttendanceStatusName.ABSENT.getValue())).count();
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
        return StudentAttendanceListResponse.builder()
                .students(studentAttendanceResponses)
                .daysInMonth(localDate.lengthOfMonth())
                .daysWithoutHolidays(daysWithoutHolidays)
                .monthDetails(getMonthDetails(localDate))
                .localDate(localDate)
                .faculty(faculty)
                .department(department)
                .subject(subject)
                .build();
    }

    public List<StudentAttendance> sortAttendanceBaseOnDay(List<StudentAttendance> studentAttendances) {
        return studentAttendances.stream().sorted().toList();
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
