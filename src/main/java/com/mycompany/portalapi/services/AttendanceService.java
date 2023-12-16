package com.mycompany.portalapi.services;


import com.mycompany.portalapi.constants.AttendanceStatusName;
import com.mycompany.portalapi.dtos.*;
import com.mycompany.portalapi.dtos.attendanceDto.AttendanceDTO;
import com.mycompany.portalapi.dtos.attendanceDto.StudentAttendanceListResponse;
import com.mycompany.portalapi.dtos.attendanceDto.DayDetails;
import com.mycompany.portalapi.dtos.attendanceDto.MonthlyAttendance;
import com.mycompany.portalapi.dtos.studentDto.StudentAttendanceResponse;
import com.mycompany.portalapi.dtos.studentDto.StudentShortInfo;
import com.mycompany.portalapi.exceptions.IllegalArgumentException;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.attendance.StudentAttendance;
import com.mycompany.portalapi.models.faculty.Department;
import com.mycompany.portalapi.models.faculty.Faculty;
import com.mycompany.portalapi.models.faculty.Semester;
import com.mycompany.portalapi.models.faculty.Subject;
import com.mycompany.portalapi.models.hrms.Student;
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
import java.util.Objects;
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

        StudentAttendance prevStudentAttendance = attendanceRepository.findByStudentIdAndDateAndSubjectAndSemester(
                attendanceDTO.studentId(),
                LocalDate.of(attendanceDTO.yearNumber(), attendanceDTO.monthNumber(), attendanceDTO.dayNumber()),
                attendanceDTO.subject(),
                attendanceDTO.semesterId()
        );


        if (prevStudentAttendance != null) {
            prevStudentAttendance.setAttendanceStatus(attendanceStatusRepository
                    .findByName(attendanceDTO.attendanceStatus())
                    .orElseThrow(() -> new IllegalArgumentException("حالت حاضری نامعتبر")));
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
                            .attendanceStatus(attendanceStatusRepository.findByName(attendanceDTO.attendanceStatus()).orElseThrow(() -> new IllegalArgumentException("حالت حاضری نامعتبر است")))
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

    public List<StudentAttendance> getStudentAttendanceBySubjectAndSemesterAndSubjectAndDate(
            Long studentId, Long subjectId, Long semester, Integer year, Integer month
    ) {
        List<StudentAttendance> studentStudentAttendances = attendanceRepository
                .findAllByStudentIdAndDateAndSemesterAndSubject(studentId, year, month, semester,subjectId);
        log.info("studentAttendanceLIst:{}"
                ,studentStudentAttendances
                        .stream().map(item ->"[" +item.getStudent().getName() + " " +
                                item.getAttendanceStatus() + "], ").collect(Collectors.joining("")));
        return studentStudentAttendances;
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
            List<StudentAttendance> studentStudentAttendances = getStudentAttendanceBySubjectAndSemesterAndSubjectAndDate
                    (student.id(), subject, semester, year, month);
            List<MonthlyAttendance> monthlyAttendances = new ArrayList<>();
            LocalDate[] fridays = getFridays(localDate.getYear(), localDate.getMonthValue());

            for (int i = 1; i <= localDate.getMonth().length(localDate.isLeapYear()); i++) {
                int finalI = i;
                Optional<StudentAttendance> monthlyAttendance = studentStudentAttendances.stream().filter(item -> item.getDate().getDayOfMonth() == finalI).findFirst();
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


            int totalPresentDays = (int) studentStudentAttendances.stream().filter(studentAttendance -> studentAttendance.getAttendanceStatus().getName().equals(AttendanceStatusName.PRESENT.getValue())).count();
            int totalAbsentDays = (int) studentStudentAttendances.stream().filter(studentAttendance -> studentAttendance.getAttendanceStatus().getName().equals(AttendanceStatusName.ABSENT.getValue())).count();
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
