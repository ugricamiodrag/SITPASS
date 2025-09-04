package com.sit.SITPass.service;

import com.sit.SITPass.DTO.FacilitySearchCriteria;
import com.sit.SITPass.model.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface FacilityService {
    Optional<Facility> getFacility(Long id);

    Facility updateFacility(Facility facility);

    void deleteFacility(Long id);

    List<Facility> getAllFacilities();

    Facility createFacility(Facility facility);
    List<Facility> getActiveFacilities(String city);

    void hideById(Long id);

    List<Facility> getFacilitiesByCriteria(FacilitySearchCriteria criteria);

    void updateAverageScore(Long id, Double averageScore);

    List<Facility> getFacilitiesByManagerId(Long id);

    List<Facility> getPopularFacilities();
    List<Facility> getVisitedFacilities(Long id);
    Page<Facility> getNewFacilities(Long id, Pageable pageable);

}
