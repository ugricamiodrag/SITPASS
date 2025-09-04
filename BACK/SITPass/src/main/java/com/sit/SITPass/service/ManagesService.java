package com.sit.SITPass.service;

import com.sit.SITPass.model.Facility;
import com.sit.SITPass.model.Manages;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ManagesService {
    int getNumberOfManagers(Facility facility);
    Optional<Manages> getManagerByFacilityId(Long id);
    Optional<Manages> getManagerByUserId(Long id, Long facilityId);

}
