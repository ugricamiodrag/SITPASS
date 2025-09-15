package com.sit.SITPass.service.impl;

import co.elastic.clients.json.JsonData;
import com.sit.SITPass.DTO.RangeDTO;
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

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ElasticsearchOperations elasticsearchTemplate;

    public Page<FacilityIndex> simpleSearch(List<String> keywords,
                                            Map<String, RangeDTO> ranges,  boolean isAsc,
                                            Pageable pageable) {
        System.out.println("Entering simpleSearch with keywords: " + keywords);
        Query query = buildSimpleSearchQuery(keywords, ranges);
        System.out.println("Built simple search query: " + query);


        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .build();
        System.out.println("NativeQuery created for simpleSearch");

        Page<FacilityIndex> result = runQuery(searchQuery, isAsc);
        System.out.println("simpleSearch returned " + result.getTotalElements() + " results");
        return result;
    }

    public Page<FacilityIndex> advancedSearch(List<String> expression,
                                              Map<String, RangeDTO> ranges, boolean isAsc,
                                              Pageable pageable) {
        System.out.println("Entering advancedSearch with expression: " + expression);
        Query query = buildAdvancedSearchQuery(expression, ranges);
        System.out.println("Built advanced search query: " + query);

        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .build();
        System.out.println("NativeQuery created for advancedSearch");

        Page<FacilityIndex> result = runQuery(searchQuery, isAsc);
        System.out.println("advancedSearch returned " + result.getTotalElements() + " results");
        return result;
    }


    public Page<FacilityIndex> MLTSearch(String expression, boolean isAsc, Pageable pageable) {
        System.out.println("Entering MLT search with expression: " + expression);
        Query query = buildMLTQuery(expression);
        System.out.println("Built MLT search query: " + query);



        NativeQuery MLTQuery = new NativeQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .build();
        System.out.println("NativeQuery created for simpleSearch");

        Page<FacilityIndex> result = runQuery(MLTQuery, isAsc);
        System.out.println("simpleSearch returned " + result.getTotalElements() + " results");
        return result;
    }


    private Query buildSimpleSearchQuery(List<String> tokens, Map<String, RangeDTO> ranges) {
        System.out.println("Building simple search query for tokens: " + tokens + " with ranges: " + ranges);

        return BoolQuery.of(q -> {

            q.must(mb -> mb.bool(b -> {

                tokens.forEach(token -> {
                    b.must(inner -> inner.bool(innerBool -> {
                        if (token.startsWith("\"") && token.endsWith("\"")) {
                            String phrase = token.substring(1, token.length() - 1);
                            innerBool.should(sb -> sb.matchPhrase(m -> m.field("name").query(phrase)));
                            innerBool.should(sb -> sb.matchPhrase(m -> m.field("description_sr").query(phrase)));
                            innerBool.should(sb -> sb.matchPhrase(m -> m.field("description_en").query(phrase)));
                            innerBool.should(sb -> sb.matchPhrase(m -> m.field("fileDescription_sr").query(phrase)));
                            innerBool.should(sb -> sb.matchPhrase(m -> m.field("fileDescription_en").query(phrase)));
                        } else if (token.endsWith("*")) {
                            String prefix = token.substring(0, token.length() - 1);
                            innerBool.should(sb -> sb.prefix(p -> p.field("name").value(prefix)));
                            innerBool.should(sb -> sb.prefix(p -> p.field("description_sr").value(prefix)));
                            innerBool.should(sb -> sb.prefix(p -> p.field("description_en").value(prefix)));
                            innerBool.should(sb -> sb.prefix(p -> p.field("fileDescription_sr").value(prefix)));
                            innerBool.should(sb -> sb.prefix(p -> p.field("fileDescription_en").value(prefix)));
                        } else if (token.startsWith("~")) {
                            String fuzzyValue = token.substring(1);
                            innerBool.should(sb -> sb.fuzzy(f -> f.field("name").value(fuzzyValue).fuzziness("1")));
                            innerBool.should(sb -> sb.fuzzy(f -> f.field("description_sr").value(fuzzyValue).fuzziness("1")));
                            innerBool.should(sb -> sb.fuzzy(f -> f.field("description_en").value(fuzzyValue).fuzziness("1")));
                            innerBool.should(sb -> sb.fuzzy(f -> f.field("fileDescription_sr").value(fuzzyValue).fuzziness("1")));
                            innerBool.should(sb -> sb.fuzzy(f -> f.field("fileDescription_en").value(fuzzyValue).fuzziness("1")));
                        } else {
                            innerBool.should(sb -> sb.match(m -> m.field("name").fuzziness("1").query(token)));
                            innerBool.should(sb -> sb.match(m -> m.field("description_sr").query(token)));
                            innerBool.should(sb -> sb.match(m -> m.field("description_en").query(token)));
                            innerBool.should(sb -> sb.match(m -> m.field("fileDescription_sr").query(token)));
                            innerBool.should(sb -> sb.match(m -> m.field("fileDescription_en").query(token)));
                            innerBool.should(sb -> sb.matchPhrase(m -> m.field("name").query(token)));
                        }
                        return innerBool;
                    }));
                });

                ranges.forEach((field, range) -> b.filter(fb -> fb.range(r -> {
                    r.field(field);
                    if (range.min() != null) r.gte(JsonData.of(range.min()));
                    if (range.max() != null) r.lte(JsonData.of(range.max()));
                    return r;
                })));

                return b;
            }));

            return q;
        })._toQuery();


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

        // Extract operation and operands
        String operation = expression.get(1).toUpperCase();
        expression.remove(1);

        String[] fieldValue1 = expression.get(0).split(":", 2);
        String field1 = fieldValue1[0].trim();
        String value1 = fieldValue1[1].replace("\"", "").trim();

        String[] fieldValue2 = expression.get(1).split(":", 2);
        String field2 = fieldValue2[0].trim();
        String value2 = fieldValue2[1].replace("\"", "").trim();

        System.out.printf("Advanced search parsed fields: %s=%s, %s=%s with operation: %s%n",
                field1, value1, field2, value2, operation);

        // Helper function to build different query types
        Function<String[], Query> buildQuery = fv -> {
            String field = fv[0];
            String value = fv[1];

            return BoolQuery.of(b -> {
                b.should(q -> q.match(m -> m.field(field).query(value)));               // normal match
                b.should(q -> q.matchPhrase(p -> p.field(field).query(value)));         // phrase query
                b.should(q -> q.prefix(p -> p.field(field).value(value.toLowerCase()))); // prefix
                b.should(q -> q.match(m -> m.field(field).query(value).fuzziness("AUTO"))); // fuzzy
                return b;
            })._toQuery();
        };

        Query leftQuery = buildQuery.apply(new String[]{field1, value1});
        Query rightQuery = buildQuery.apply(new String[]{field2, value2});

        // Build bool query with logical operator

        return BoolQuery.of(b -> {
            switch (operation) {
                case "AND" -> {
                    b.must(leftQuery);
                    b.must(rightQuery);
                }
                case "OR" -> {
                    b.should(leftQuery);
                    b.should(rightQuery);
                    b.minimumShouldMatch("1");
                }
                case "NOT" -> {
                    b.must(leftQuery);
                    b.mustNot(rightQuery);
                }
                default -> throw new IllegalArgumentException("Unsupported operator: " + operation);
            }

            // Add range filters if present
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


    private Page<FacilityIndex> runQuery(NativeQuery searchQuery,  boolean isAsc) {
        System.out.println("Running query: " + searchQuery.getQuery());
        var searchHits = elasticsearchTemplate.search(searchQuery, FacilityIndex.class,
                IndexCoordinates.of("facility_index"));
        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());
        System.out.println("Query returned " + searchHits.get().toList().size() + " hits");

        // Map SearchHit<FacilityIndex> to FacilityIndex and create a new PageImpl
        List<FacilityIndex> content = searchHitsPaged.getContent().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        if (isAsc) {
            content.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        } else {
            content.sort((o1, o2) -> o2.getName().compareToIgnoreCase(o1.getName()));
        }



        return new PageImpl<>(content, searchHitsPaged.getPageable(), searchHitsPaged.getTotalElements());
    }

    private Query buildMLTQuery(String expression) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException("Expression must not be empty");
        }

        return BoolQuery.of(b -> b
                .should(s -> s.moreLikeThis(mlt -> mlt
                        .fields("name", "description_sr", "description_en", "fileDescription_sr", "fileDescription_en")
                        .like(l -> l.text(expression))
                        .minTermFreq(1)
                        .minDocFreq(1)
                ))
        )._toQuery();
    }

}
