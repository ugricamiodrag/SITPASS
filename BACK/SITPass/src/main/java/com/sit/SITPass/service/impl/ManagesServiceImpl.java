package com.sit.SITPass.service.impl;

import com.sit.SITPass.model.Facility;
import com.sit.SITPass.model.Manages;
import com.sit.SITPass.repository.ManagesRepository;
import com.sit.SITPass.service.ManagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ManagesServiceImpl implements ManagesService {

    @Autowired
    private ManagesRepository managesRepository;

    @Override
    public int getNumberOfManagers(Facility facility) {
        return managesRepository.countByFacilityIdAndEndDateIsNull(facility.getId());
    }

    @Override
    public Optional<Manages> getManagerByFacilityId(Long id) {
        return managesRepository.findByFacilityIdAndEndDateIsNull(id);
    }

    @Override
    public Optional<Manages> getManagerByUserId(Long id, Long facilityId) {
        return managesRepository.findByUserId(id, facilityId);
    }
}
