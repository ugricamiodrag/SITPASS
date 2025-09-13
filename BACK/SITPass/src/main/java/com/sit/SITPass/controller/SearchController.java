package com.sit.SITPass.controller;

import com.sit.SITPass.DTO.SearchQueryDTO;
import com.sit.SITPass.index.FacilityIndex;
import com.sit.SITPass.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/simple")
    public Page<FacilityIndex> simpleSearch(
                                            @RequestBody SearchQueryDTO simpleSearchQuery,
                                            Pageable pageable) {
        return searchService.simpleSearch(simpleSearchQuery.keywords(), simpleSearchQuery.ranges(), pageable);
    }

    @PostMapping("/advanced")
    public Page<FacilityIndex> advancedSearch(@RequestBody SearchQueryDTO advancedSearchQuery,
                                           Pageable pageable) {
        return searchService.advancedSearch(advancedSearchQuery.keywords(), advancedSearchQuery.ranges() ,pageable);
    }
}
