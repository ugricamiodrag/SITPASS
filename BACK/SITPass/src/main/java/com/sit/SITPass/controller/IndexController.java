package com.sit.SITPass.controller;

import com.sit.SITPass.DTO.FacilityDocumentFileDTO;
import com.sit.SITPass.DTO.FacilityDocumentFileResponse;
import com.sit.SITPass.service.IndexingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/index")
@RequiredArgsConstructor
public class IndexController {

    private final IndexingService indexingService;

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public FacilityDocumentFileResponse addDocumentFile(
            @PathVariable Long id,
            @ModelAttribute FacilityDocumentFileDTO documentFile) {

        MultipartFile file = documentFile != null ? documentFile.file() : null;
        String serverFilename = indexingService.indexDocument(id, file);
        return new FacilityDocumentFileResponse(serverFilename);
    }

}
