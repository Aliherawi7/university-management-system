package com.mycompany.portalapi.services.mappers;

import com.mycompany.portalapi.dtos.LocationDTO;
import com.mycompany.portalapi.models.hrms.Location;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class LocationDTOMapper implements Function<Location, LocationDTO> {
    @Override
    public LocationDTO apply(Location location) {
        return LocationDTO
                .builder()
                .city(location.getCity())
                .district(location.getDistrict())
                .villageOrQuarter(location.getVillageOrQuarter())
                .current(location.isCurrent())
                .build();
    }
}
