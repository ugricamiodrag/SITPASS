package com.sit.SITPass.service;

import com.sit.SITPass.model.Facility;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface IndexingService {
    String indexDocument(Long facilityId, MultipartFile documentFile);
    void updateRatingsInFacility(Facility facility);
}
