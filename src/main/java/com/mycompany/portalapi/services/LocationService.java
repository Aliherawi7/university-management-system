package com.mycompany.portalapi.services;

import com.mycompany.portalapi.dtos.LocationDTO;
import com.mycompany.portalapi.dtos.Locations;
import com.mycompany.portalapi.models.Location;
import com.mycompany.portalapi.repositories.LocationRepository;
import lombok.AllArgsConstructor;
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
