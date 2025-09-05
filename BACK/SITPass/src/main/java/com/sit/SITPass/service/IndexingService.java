package com.sit.SITPass.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface IndexingService {
    String indexDocument(Long facilityId, MultipartFile documentFile);
}
