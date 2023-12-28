package com.mycompany.umsapi.services;

import com.mycompany.umsapi.dtos.studentDto.RelativeRegistrationDTO;
import com.mycompany.umsapi.exceptions.IllegalArgumentException;
import com.mycompany.umsapi.models.hrms.Relationship;
import com.mycompany.umsapi.models.hrms.Relative;
import com.mycompany.umsapi.models.hrms.Student;
import com.mycompany.umsapi.repositories.RelationshipRepository;
import com.mycompany.umsapi.repositories.RelativeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelativeService {
    private final RelativeRepository relativeRepository;
    private final RelationshipRepository relationshipRepository;


    public void addRelative(Relative relative) {
        relativeRepository.save(relative);
    }

    public List<Relative> getAllStudentRelativesById(Long studentId) {
        return relativeRepository.findAllByStudentId(studentId);
    }

    public void addStudentRelatives(Student student, List<RelativeRegistrationDTO> relatives) {
        relatives.forEach(item -> {
            Relationship relationship = relationshipRepository
                    .findById(item.relationshipId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid relationship id!"));
            Relative relative = Relative
                    .builder()
                    .job(item.job())
                    .student(student)
                    .jobLocation(item.jobLocation())
                    .phoneNumber(item.phoneNumber())
                    .relationship(relationship)
                    .name(item.name())
                    .build();
            addRelative(relative);
        });
    }
    public void updateStudentRelatives(Student student, List<RelativeRegistrationDTO> relatives) {
        List<Relative> relativeList = getAllStudentRelativesById(student.getId());
        relatives.forEach(item -> {
            Relationship relationship = relationshipRepository
                    .findById(item.relationshipId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Relationship id!"));
            Relative relative = relativeList.stream()
                    .filter(r -> r.getRelationship().equals(relationship))
                    .findFirst().get();
            relative.setName(item.name());
            relative.setJob(item.job());
            relative.setPhoneNumber(item.phoneNumber());
            relative.setJobLocation(item.jobLocation());
            addRelative(relative);
        });
    }

    public void deleteAllRelativesByStudent(Long studentId) {
        getAllStudentRelativesById(studentId).forEach(relative -> {
            relativeRepository.deleteById(relative.getId());
        });
    }
}
