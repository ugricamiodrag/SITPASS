package com.sit.SITPass.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sit.SITPass.DTO.FacilityDTO;
import com.sit.SITPass.DTO.UserRequestDTO;
import com.sit.SITPass.model.AccountRequest;
import com.sit.SITPass.service.AccountRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private AccountRequestService requestService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getRequests(@RequestBody Map<String, Integer> requestBody) {
        int page = requestBody.getOrDefault("page", 0);
        int pageSize = requestBody.getOrDefault("pageSize", 10);

        Page<AccountRequest> requestPage = requestService.getRequests(page, pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("requests", requestPage.getContent());
        response.put("totalItems", requestPage.getTotalElements());
        response.put("totalPages", requestPage.getTotalPages());
        response.put("currentPage", requestPage.getNumber());

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/accept", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> acceptRequest(@RequestBody AccountRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            requestService.acceptRequest(request.getEmail());
            response.put("message", "Request accepted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Failed to accept request");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping(value = "/decline", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> declineRequest(@RequestBody Map<String, Object> payload) {
        Map<String, String> response = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            UserRequestDTO request = mapper.convertValue(payload.get("request"), UserRequestDTO.class);
            String reason = (String) payload.get("reason");

            requestService.rejectRequest(request.getEmail(), reason);

            response.put("message", "Request declined successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Failed to decline request");
            return ResponseEntity.status(500).body(response);
        }
    }





}
