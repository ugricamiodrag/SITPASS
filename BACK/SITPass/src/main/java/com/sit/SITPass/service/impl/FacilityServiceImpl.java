package com.sit.SITPass.service.impl;

import com.sit.SITPass.DTO.FacilitySearchCriteria;
import com.sit.SITPass.model.Facility;
import com.sit.SITPass.repository.FacilityRepository;
import com.sit.SITPass.service.FacilityService;
import com.sit.SITPass.specifications.FacilitySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class FacilityServiceImpl implements FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    @Override
    public Optional<Facility> getFacility(Long id) {
        return facilityRepository.findById(id);
    }

    @Override
    public Facility updateFacility(Facility facility) {
        return facilityRepository.save(facility);
    }

    @Override
    public void deleteFacility(Long id) {
        facilityRepository.deleteById(id);
    }

    @Override
    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    @Override
    public Facility createFacility(Facility facility) {
        return facilityRepository.save(facility);
    }

    @Override
    public List<Facility> getActiveFacilities(String city) {
        return facilityRepository.getFacilitiesByActiveTrue(city);
    }

    @Override
    public void hideById(Long id) {
        facilityRepository.hideById(id);
    }

    @Override
    public List<Facility> getFacilitiesByCriteria(FacilitySearchCriteria criteria) {
        FacilitySpecification spec = new FacilitySpecification(criteria);

        return facilityRepository.findAll(spec);
    }

    @Override
    public void updateAverageScore(Long id, Double averageScore) {
        Optional<Facility> facility = getFacility(id);
        if (facility.isPresent()) {
            if (facility.get().getTotalRating() == 0) {
                facility.get().setTotalRating(averageScore);
            }
            else {
                facility.get().setTotalRating((facility.get().getTotalRating() + averageScore) / 2);
            }
            
            facilityRepository.save(facility.get());
        }
    }

    @Override
    public List<Facility> getFacilitiesByManagerId(Long id) {
        return facilityRepository.findFacilitiesByManagerId(id);
    }

    @Override
    public List<Facility> getPopularFacilities() {
        return facilityRepository.findAllByPopularity();
    }

    @Override
    public List<Facility> getVisitedFacilities(Long id) {
        return facilityRepository.getVisitedFacilities(id);
    }

    @Override
    public Page<Facility> getNewFacilities(Long id, Pageable pageable) {
        return facilityRepository.getNewFacilities(id, pageable);
    }
}
