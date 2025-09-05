package com.sit.SITPass.service;

import com.sit.SITPass.index.FacilityIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchService {
    Page<FacilityIndex> simpleSearch(List<String> keywords, Pageable pageable);

    Page<FacilityIndex> advancedSearch(List<String> expression, Pageable pageable);
}
