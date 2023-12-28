package com.mycompany.umsapi.services;

import com.mycompany.umsapi.dtos.LocationDTO;
import com.mycompany.umsapi.dtos.Locations;
import com.mycompany.umsapi.models.hrms.Location;
import com.mycompany.umsapi.models.hrms.Student;
import com.mycompany.umsapi.repositories.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public void addLocation(Location location){
        locationRepository.save(location);
    }
    public void addStudentLocation(Student student, LocationDTO locationDTO){
        Location location = Location.builder()
                .student(student)
                .city(locationDTO.city())
                .district(locationDTO.district())
                .villageOrQuarter(locationDTO.villageOrQuarter())
                .current(locationDTO.current()).build();
        addLocation(location);
    }
    public void updateStudentLocation(Student student, Locations locations){
        List<Location> studentLocations = getAllLocationsByStudentId(student.getId());
        studentLocations.forEach(item -> {
            if (item.isCurrent()) {
                item.setCity(locations.current().city());
                item.setDistrict(locations.current().city());
                item.setVillageOrQuarter(locations.current().city());
            } else {
                item.setCity(locations.previous().city());
                item.setDistrict(locations.previous().city());
                item.setVillageOrQuarter(locations.previous().city());
            }
            addLocation(item);
        });
    }



    public List<Location> getAllLocationsByStudentId(Long studentId){
        return locationRepository.findAllByStudentId(studentId);
    }

    public Locations getCurrentAndPreviousLocationsOfStudent(Long studentId){
        Location current = locationRepository.findByStudentIdAndCurrent(studentId, true);
        Location previous = locationRepository.findByStudentIdAndCurrent(studentId, false);

        return Locations.builder()
                .current(LocationDTO
                        .builder()
                        .villageOrQuarter(current.getVillageOrQuarter())
                        .district(current.getDistrict())
                        .city(current.getCity())
                        .current(current.isCurrent())
                        .build())
                .previous(
                        LocationDTO
                                .builder()
                                .villageOrQuarter(previous.getVillageOrQuarter())
                                .district(previous.getDistrict())
                                .city(previous.getCity())
                                .current(previous.isCurrent())
                                .build()
                ).build();
    }

    public void deleteLocationsByStudentId(Long studentId){
        locationRepository.deleteAll(getAllLocationsByStudentId(studentId));
    }
}
