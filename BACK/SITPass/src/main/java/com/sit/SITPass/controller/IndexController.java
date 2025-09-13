package com.sit.SITPass.controller;

import com.sit.SITPass.DTO.FacilityDocumentFileDTO;
import com.sit.SITPass.DTO.FacilityDocumentFileResponse;
import com.sit.SITPass.index.FacilityIndex;
import com.sit.SITPass.indexRepository.FacilityIndexRepository;
import com.sit.SITPass.service.IndexingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/index")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequiredArgsConstructor
public class IndexController {

    private final IndexingService indexingService;
    private final FacilityIndexRepository repo;

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public FacilityDocumentFileResponse addDocumentFile(
            @PathVariable Long id,
            @ModelAttribute FacilityDocumentFileDTO documentFile) {

        MultipartFile file = documentFile != null ? documentFile.file() : null;
        String serverFilename = indexingService.indexDocument(id, file);
        return new FacilityDocumentFileResponse(serverFilename);
    }

    @GetMapping("/{id}")
    public FacilityDocumentFileResponse getDocumentFile(@PathVariable String id) {
        FacilityIndex facilityIndex = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Facility not found in index"));

        return new FacilityDocumentFileResponse(facilityIndex.getServerFilename());
    }

}
