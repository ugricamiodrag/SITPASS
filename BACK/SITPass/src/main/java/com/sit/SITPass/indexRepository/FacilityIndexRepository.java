package com.sit.SITPass.indexRepository;

import com.sit.SITPass.index.FacilityIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityIndexRepository extends ElasticsearchRepository<FacilityIndex, String> {
}
