package com.sit.SITPass.service;

import com.sit.SITPass.DTO.RangeDTO;
import com.sit.SITPass.index.FacilityIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SearchService {
    Page<FacilityIndex> simpleSearch(List<String> keywords,
                                     Map<String, RangeDTO> ranges, boolean isAsc,
                                     Pageable pageable);

    Page<FacilityIndex> advancedSearch(List<String> tokens, Map<String, RangeDTO> ranges,  boolean isAsc, Pageable pageable);
    Page<FacilityIndex> MLTSearch(String expression, boolean isAsc, Pageable pageable);
}
