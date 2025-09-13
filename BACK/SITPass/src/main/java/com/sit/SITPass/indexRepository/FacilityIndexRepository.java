package com.sit.SITPass.indexRepository;

import com.sit.SITPass.index.FacilityIndex;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityIndexRepository extends ElasticsearchRepository<FacilityIndex, String> {

    @NotNull
    Optional<FacilityIndex> findById(@NotNull String id);


}
