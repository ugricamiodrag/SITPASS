package com.sit.SITPass.service.impl;

import co.elastic.clients.json.JsonData;
import com.sit.SITPass.DTO.RangeDTO;
import com.sit.SITPass.DTO.SearchQueryDTO;
import com.sit.SITPass.index.FacilityIndex;
import com.sit.SITPass.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ElasticsearchOperations elasticsearchTemplate;


    public Page<FacilityIndex> simpleSearch(List<String> keywords,
                                            Map<String, RangeDTO> ranges,
                                            Pageable pageable) {
        Query query = buildSimpleSearchQuery(keywords, ranges);
        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .build();
        return runQuery(searchQuery);
    }


    public Page<FacilityIndex> advancedSearch(List<String> expression,
                                              Map<String, RangeDTO> ranges,
                                              Pageable pageable) {
        Query query = buildAdvancedSearchQuery(expression, ranges);
        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .build();
        return runQuery(searchQuery);
    }


    private Query buildSimpleSearchQuery(List<String> tokens, Map<String, RangeDTO> ranges) {
        return co.elastic.clients.elasticsearch._types.query_dsl.Query.of(q ->
                q.bool(b -> {
                    // MUST queries for all tokens
                    for (String token : tokens) {
                        b.should(s -> s.match(m -> m.field("name").query(token).fuzziness(Fuzziness.ONE.asString())));
                        b.should(s -> s.match(m -> m.field("descriptionSr").query(token)));
                        b.should(s -> s.match(m -> m.field("descriptionEn").query(token)));
                        b.should(s -> s.match(m -> m.field("fileDescriptionSr").query(token)));
                        b.should(s -> s.match(m -> m.field("fileDescriptionEn").query(token)));

                        // Match phrase query
                        b.should(s -> s.matchPhrase(mp -> mp.field("name").query(token)));

                        // Prefix query
                        b.should(s -> s.prefix(p -> p.field("name").value(token)));
                        b.should(s -> s.prefix(p -> p.field("descriptionSr").value(token)));
                        b.should(s -> s.prefix(p -> p.field("descriptionEn").value(token)));
                        b.should(s -> s.prefix(p -> p.field("fileDescriptionSr").value(token)));
                        b.should(s -> s.prefix(p -> p.field("fileDescriptionEn").value(token)));

                        // Fuzzy query
                        b.should(s -> s.fuzzy(f -> f.field("name").value(token).fuzziness(String.valueOf(Fuzziness.ONE))));

                        // More Like This
                        b.should(s -> s.moreLikeThis(mlt -> mlt
                                .fields("name", "descriptionSr", "descriptionEn", "fileDescriptionSr", "fileDescriptionEn")
                                .like(l -> l.text(token))
                                .minTermFreq(1)
                                .minDocFreq(1)
                        ));
                    }

                    // RANGE filters
                    for (Map.Entry<String, RangeDTO> entry : ranges.entrySet()) {
                        String field = entry.getKey();
                        RangeDTO range = entry.getValue();
                        b.filter(f -> f.range(r -> {
                            r.field(field);
                            if (range.min() != null) r.gte(JsonData.of(range.min()));
                            if (range.max() != null) r.lte(JsonData.of(range.max()));
                            return r;
                        }));
                    }

                    return b;
                })
        );
    }




    private Query buildAdvancedSearchQuery(List<String> expression, Map<String, RangeDTO> ranges) {
        if (expression.size() != 3) {
            throw new IllegalArgumentException("Expression must contain 3 elements: operand1, operation, operand2");
        }

        String operation = expression.get(1);
        expression.remove(1); // remove operation from list

        String[] fieldValue1 = expression.get(0).split(":", 2);
        String field1 = fieldValue1[0];
        String value1 = fieldValue1[1];

        String[] fieldValue2 = expression.get(1).split(":", 2);
        String field2 = fieldValue2[0];
        String value2 = fieldValue2[1];

        return BoolQuery.of(b -> {
            switch (operation.toUpperCase()) {
                case "AND":
                    b.must(m -> m.match(mm -> mm.field(field1).query(value1).fuzziness(Fuzziness.ONE.asString())));
                    b.must(m -> m.match(mm -> mm.field(field2).query(value2)));
                    break;
                case "OR":
                    b.should(s -> s.match(mm -> mm.field(field1).query(value1).fuzziness(Fuzziness.ONE.asString())));
                    b.should(s -> s.match(mm -> mm.field(field2).query(value2)));
                    break;
                case "NOT":
                    b.must(m -> m.match(mm -> mm.field(field1).query(value1).fuzziness(Fuzziness.ONE.asString())));
                    b.mustNot(m -> m.match(mm -> mm.field(field2).query(value2)));
                    break;
            }

            // Apply dynamic range filters
            for (Map.Entry<String, RangeDTO> entry : ranges.entrySet()) {
                String field = entry.getKey();
                RangeDTO range = entry.getValue();
                b.filter(f -> {
                    RangeQuery.Builder rangeQuery = new RangeQuery.Builder().field(field);
                    if (range.min() != null) rangeQuery.gte(JsonData.of(range.min()));
                    if (range.max() != null) rangeQuery.lte(JsonData.of(range.max()));
                    return f.range(rangeQuery.build());
                });
            }
            return b;
        })._toQuery();
    }


    private Page<FacilityIndex> runQuery(NativeQuery searchQuery) {
        var searchHits = elasticsearchTemplate.search(searchQuery, FacilityIndex.class,
                IndexCoordinates.of("facility_index"));
        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());
        return (Page<FacilityIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }
}
