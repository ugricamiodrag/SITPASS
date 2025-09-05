package com.sit.SITPass.service.impl;

import com.sit.SITPass.index.FacilityIndex;
import com.sit.SITPass.indexRepository.FacilityIndexRepository;
import com.sit.SITPass.model.Facility;
import com.sit.SITPass.repository.FacilityRepository;
import com.sit.SITPass.service.FileService;
import com.sit.SITPass.service.IndexingService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IndexingServiceImpl implements IndexingService {

    private final FacilityIndexRepository indexRepo;
    private final FacilityRepository facilityRepo;
    private final FileService fileService;
    private final LanguageDetector languageDetector;

    @Override
    @Transactional
    public String indexDocument(Long facilityId, MultipartFile documentFile) {
        Facility facility = facilityRepo.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found: " + facilityId));

        // 2) Store file
        String serverFilename = fileService.store(documentFile, UUID.randomUUID().toString());

        // 3) Extract file text

        String content = null;
        try {
            content = extractDocumentContent(documentFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String lang = detectLanguage(content);

        // 4) Build ES index entry
        FacilityIndex idx = new FacilityIndex();
        idx.setId(UUID.randomUUID().toString());
        idx.setName(facility.getName());
        idx.setServerFilename(serverFilename);


        // File descriptions
        if ("sr".equalsIgnoreCase(lang)) {
            idx.setFileDescriptionSr(content);
        } else {
            idx.setFileDescriptionEn(content);
        }

        //TODO Facility grades
//        idx.setReviewCount(facility.getReviewCount());
//        idx.setAvgEquipmentGrade(facility.getAvgEquipmentGrade());
//        idx.setAvgStaffGrade(facility.getAvgStaffGrade());
//        idx.setAvgHygieneGrade(facility.getAvgHygieneGrade());
//        idx.setAvgSpaceGrade(facility.getAvgSpaceGrade());

        // 5) Save to Elasticsearch
        indexRepo.save(idx);

        return serverFilename;
    }

    private String extractDocumentContent(MultipartFile multipartPdfFile) throws Exception {
        String documentContent;
        try (var pdfFile = multipartPdfFile.getInputStream()) {
            var pdDocument = PDDocument.load(pdfFile);
            var textStripper = new PDFTextStripper();
            documentContent = textStripper.getText(pdDocument);
            pdDocument.close();
        } catch (IOException e) {
            throw new Exception("Error while trying to load PDF file content.");
        }

        return documentContent;
    }

    private String detectLanguage(String text) {
        var detectedLanguage = languageDetector.detect(text).getLanguage().toUpperCase();
        if (detectedLanguage.equals("HR")) {
            detectedLanguage = "SR";
        }

        return detectedLanguage;
    }
}
