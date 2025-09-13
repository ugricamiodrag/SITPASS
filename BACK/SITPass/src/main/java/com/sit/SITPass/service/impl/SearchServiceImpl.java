package com.sit.SITPass.service.impl;

import co.elastic.clients.json.JsonData;
import com.sit.SITPass.DTO.RangeDTO;
import com.sit.SITPass.DTO.SearchQueryDTO;
import com.sit.SITPass.index.FacilityIndex;
import com.sit.SITPass.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
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

        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {


                if (token.startsWith("\"") && token.endsWith("\"")) {
                    String phrase = token.substring(1, token.length() - 1);
                    b.should(sb -> sb.matchPhrase(m -> m.field("name").query(phrase)));
                    b.should(sb -> sb.matchPhrase(m -> m.field("description_sr").query(phrase)));
                    b.should(sb -> sb.matchPhrase(m -> m.field("description_en").query(phrase)));
                    b.should(sb -> sb.matchPhrase(m -> m.field("fileDescription_sr").query(phrase)));
                    b.should(sb -> sb.matchPhrase(m -> m.field("fileDescription_en").query(phrase)));
                }

                else if (token.endsWith("*")) {
                    String prefix = token.substring(0, token.length() - 1);
                    b.should(sb -> sb.prefix(p -> p.field("name").value(prefix)));
                    b.should(sb -> sb.prefix(p -> p.field("description_sr").value(prefix)));
                    b.should(sb -> sb.prefix(p -> p.field("description_en").value(prefix)));
                    b.should(sb -> sb.prefix(p -> p.field("fileDescription_sr").value(prefix)));
                    b.should(sb -> sb.prefix(p -> p.field("fileDescription_en").value(prefix)));
                }

                else if (token.startsWith("~")) {
                    String fuzzyValue = token.substring(1);
                    b.should(sb -> sb.fuzzy(f -> f.field("name").value(fuzzyValue).fuzziness("1")));
                    b.should(sb -> sb.fuzzy(f -> f.field("description_sr").value(fuzzyValue).fuzziness("1")));
                    b.should(sb -> sb.fuzzy(f -> f.field("description_en").value(fuzzyValue).fuzziness("1")));
                    b.should(sb -> sb.fuzzy(f -> f.field("fileDescription_sr").value(fuzzyValue).fuzziness("1")));
                    b.should(sb -> sb.fuzzy(f -> f.field("fileDescription_en").value(fuzzyValue).fuzziness("1")));
                }
                // Default -> Match + MatchPhrase
                else {
                    b.should(sb -> sb.match(m -> m.field("name").fuzziness("1").query(token)));
                    b.should(sb -> sb.match(m -> m.field("description_sr").query(token)));
                    b.should(sb -> sb.match(m -> m.field("description_en").query(token)));
                    b.should(sb -> sb.match(m -> m.field("fileDescription_sr").query(token)));
                    b.should(sb -> sb.match(m -> m.field("fileDescription_en").query(token)));
                    b.should(sb -> sb.matchPhrase(m -> m.field("name").query(token)));
                }
            });


            ranges.forEach((field, range) -> {
                b.filter(fb -> fb.range(r -> {
                    r.field(field);
                    if (range.min() != null) r.gte(JsonData.of(range.min()));
                    if (range.max() != null) r.lte(JsonData.of(range.max()));
                    return r;
                }));
            });

            return b;
        })))._toQuery();


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

        // Map SearchHit<FacilityIndex> to FacilityIndex and create a new PageImpl
        List<FacilityIndex> content = searchHitsPaged.getContent().stream()
                .map(SearchHit::getContent)
                .toList();

        return new PageImpl<>(content, searchHitsPaged.getPageable(), searchHitsPaged.getTotalElements());
    }

}
