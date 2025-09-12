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
        System.out.println("Entering simpleSearch with keywords: " + keywords);
        Query query = buildSimpleSearchQuery(keywords, ranges);
        System.out.println("Built simple search query: " + query);



        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .build();
        System.out.println("NativeQuery created for simpleSearch");

        Page<FacilityIndex> result = runQuery(searchQuery);
        System.out.println("simpleSearch returned " + result.getTotalElements() + " results");
        return result;
    }

    public Page<FacilityIndex> advancedSearch(List<String> expression,
                                              Map<String, RangeDTO> ranges,
                                              Pageable pageable) {
        System.out.println("Entering advancedSearch with expression: " + expression);
        Query query = buildAdvancedSearchQuery(expression, ranges);
        System.out.println("Built advanced search query: " + query);

        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .build();
        System.out.println("NativeQuery created for advancedSearch");

        Page<FacilityIndex> result = runQuery(searchQuery);
        System.out.println("advancedSearch returned " + result.getTotalElements() + " results");
        return result;
    }

    private Query buildSimpleSearchQuery(List<String> tokens, Map<String, RangeDTO> ranges) {
        System.out.println("Building simple search query for tokens: " + tokens + " with ranges: " + ranges);

        return BoolQuery.of(b -> {
            tokens.forEach(token -> {
                b.should(s -> s.match(m -> m.field("name").query(token)));
                b.should(s -> s.match(m -> m.field("descriptionSr").query(token)));
                b.should(s -> s.match(m -> m.field("descriptionEn").query(token)));
            });
            return b;
        })._toQuery();

//        return Query.of(q ->
//                q.bool(b -> {
//                    for (String token : tokens) {
//                        if (token == null || token.trim().isEmpty()) continue;
//                        token = token.trim();
//
//                        // osnovni match
//                        String finalToken = token;
//                        b.should(s -> s.match(m -> m.field("name").query(finalToken).fuzziness(Fuzziness.ONE.asString())));
//                        b.should(s -> s.match(m -> m.field("descriptionSr").query(finalToken)));
//                        b.should(s -> s.match(m -> m.field("descriptionEn").query(finalToken)));
//                        b.should(s -> s.match(m -> m.field("fileDescriptionSr").query(finalToken)));
//                        b.should(s -> s.match(m -> m.field("fileDescriptionEn").query(finalToken)));
//
//                        // phrase match
//                        b.should(s -> s.matchPhrase(mp -> mp.field("name").query(finalToken)));
//
//                        // prefix – samo ako token ima neku dužinu
//                        if (token.length() > 2) {
//                            b.should(s -> s.prefix(p -> p.field("name").value(finalToken)));
//                            b.should(s -> s.prefix(p -> p.field("descriptionSr").value(finalToken)));
//                            b.should(s -> s.prefix(p -> p.field("descriptionEn").value(finalToken)));
//                            b.should(s -> s.prefix(p -> p.field("fileDescriptionSr").value(finalToken)));
//                            b.should(s -> s.prefix(p -> p.field("fileDescriptionEn").value(finalToken)));
//                        }
//
//                        // fuzzy – samo ako token ima neku dužinu
//                        if (token.length() > 2) {
//                            b.should(s -> s.fuzzy(f -> f.field("name").value(finalToken).fuzziness("1")));
//                        }
//
//                        // moreLikeThis – obično ignoriši ako je prekratak token
//                        if (token.length() > 3) {
//                            b.should(s -> s.moreLikeThis(mlt -> mlt
//                                    .fields("name", "descriptionSr", "descriptionEn", "fileDescriptionSr", "fileDescriptionEn")
//                                    .like(l -> l.text(finalToken))
//                                    .minTermFreq(1)
//                                    .minDocFreq(1)
//                            ));
//                        }
//                    }
//
//                    for (Map.Entry<String, RangeDTO> entry : ranges.entrySet()) {
//                        String field = entry.getKey();
//                        RangeDTO range = entry.getValue();
//                        b.filter(f -> f.range(r -> {
//                            r.field(field);
//                            if (range.min() != null) r.gte(JsonData.of(range.min()));
//                            if (range.max() != null) r.lte(JsonData.of(range.max()));
//                            return r;
//                        }));
//                    }
//
//                    return b;
//                })
//        );
    }


    private Query buildAdvancedSearchQuery(List<String> expression, Map<String, RangeDTO> ranges) {
        System.out.println("Building advanced search query for expression: " + expression + " and ranges: " + ranges);
        if (expression.size() != 3) {
            throw new IllegalArgumentException("Expression must contain 3 elements: operand1, operation, operand2");
        }

        String operation = expression.get(1);
        expression.remove(1);

        String[] fieldValue1 = expression.get(0).split(":", 2);
        String field1 = fieldValue1[0];
        String value1 = fieldValue1[1];

        String[] fieldValue2 = expression.get(1).split(":", 2);
        String field2 = fieldValue2[0];
        String value2 = fieldValue2[1];

        System.out.println("Advanced search parsed fields: " + field1 + "=" + value1 + ", " + field2 + "=" + value2 + " with operation: " + operation);

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
        System.out.println("Running query: " + searchQuery.getQuery());
        var searchHits = elasticsearchTemplate.search(searchQuery, FacilityIndex.class,
                IndexCoordinates.of("facility_index"));
        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());
        System.out.println("Query returned " + searchHits.get().toList().size() + " hits");
        return (Page<FacilityIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }
}
