package com.mycompany.portalapi.services;

import com.mycompany.portalapi.models.Relative;
import com.mycompany.portalapi.repositories.RelativeRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelativeService {
    private final RelativeRepository relativeRepository;


    public void addRelative(Relative relative){
        relativeRepository.save(relative);
    }

    public List<Relative> getAllStudentRelativesById(Long studentId){
        return relativeRepository.findAllByStudentId(studentId);
    }
}
