package com.sit.SITPass.controller;

import com.sit.SITPass.DTO.AnalyticsDTO;
import com.sit.SITPass.DTO.AnalyticsData;
import com.sit.SITPass.DTO.AnalyticsResponseDTO;
import com.sit.SITPass.DTO.TimePeriodData;
import com.sit.SITPass.service.ExerciseService;
import com.sit.SITPass.service.FacilityService;
import com.sit.SITPass.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ExerciseService exerciseService;


    @PostMapping(value = "/reviews")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<AnalyticsData>> getAnalyticsData(@RequestBody AnalyticsDTO analyticsDTO){
        List<AnalyticsData> data = reviewService.getAnalyticsData(analyticsDTO.getFacilityId(), analyticsDTO.getFrom(), analyticsDTO.getTo());
        return ResponseEntity.ok(data);

    }

    @PostMapping(value = "/users")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<TimePeriodData>> getTimePeriodData(@RequestBody AnalyticsDTO analyticsDTO){
        List<TimePeriodData> tpd = exerciseService.getTimePeriodData(analyticsDTO.getFacilityId(), analyticsDTO.getFrom(), analyticsDTO.getTo());
        return ResponseEntity.ok(tpd);

    }

    @PostMapping(value = "/custom")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public ResponseEntity<AnalyticsResponseDTO> getCustom(@RequestBody AnalyticsDTO analyticsDTO){
        List<AnalyticsData> data = reviewService.getAnalyticsData(analyticsDTO.getFacilityId(), analyticsDTO.getFrom(), analyticsDTO.getTo());
        List<TimePeriodData> tpd = exerciseService.getTimePeriodData(analyticsDTO.getFacilityId(), analyticsDTO.getFrom(), analyticsDTO.getTo());
        AnalyticsResponseDTO analyticsResponseDTO = new AnalyticsResponseDTO(data, tpd);
        return ResponseEntity.ok(analyticsResponseDTO);

    }

}
