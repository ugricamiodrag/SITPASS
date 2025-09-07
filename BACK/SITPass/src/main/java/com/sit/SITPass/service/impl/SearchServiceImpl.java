package com.sit.SITPass.service.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final ElasticsearchOperations elasticsearchTemplate;

    @Override
    public Page<FacilityIndex> simpleSearch(List<String> keywords, Pageable pageable) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildSimpleSearchQuery(keywords))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<FacilityIndex> advancedSearch(List<String> expression, Pageable pageable) {
        if (expression.size() != 3) {
            throw new IllegalArgumentException("Expression size must be 3");
        }

        String operation = expression.get(1);
        expression.remove(1);
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildAdvancedSearchQuery(expression, operation))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    private Query buildSimpleSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                // Term Query - simplest
                // Matches documents with exact term in "title" field
                b.should(sb -> sb.term(m -> m.field("name").value(token)));

                // Terms Query
                // Matches documents with any of the specified terms in "title" field
//                var terms = new ArrayList<>(List.of("dummy1", "dummy2"));
//                var titleTerms = new TermsQueryField.Builder()
//                    .value(terms.stream().map(FieldValue::of).toList())
//                    .build();
//                b.should(sb -> sb.terms(m -> m.field("title").terms(titleTerms)));

                // Match Query - full-text search with fuzziness
                // Matches documents with fuzzy matching in "title" field
                b.should(sb -> sb.match(
                        m -> m.field("name").fuzziness(Fuzziness.ONE.asString()).query(token)));

                b.should(sb -> sb.match(
                        m -> m.field("description").fuzziness(Fuzziness.ONE.asString()).query(token)
                ));

                // Match Query - full-text search in other fields
                // Matches documents with full-text search in other fields
                b.should(sb -> sb.match(m -> m.field("content_sr").query(token).boost(0.5f)));
                b.should(sb -> sb.match(m -> m.field("content_en").query(token)));

                // Wildcard Query - unsafe
                // Matches documents with wildcard matching in "title" field
                b.should(sb -> sb.wildcard(m -> m.field("name").value("*" + token + "*")));
                b.should(sb -> sb.wildcard(m -> m.field("description").value("*" + token + "*")));
                // Regexp Query - unsafe
                // Matches documents with regular expression matching in "title" field
//                b.should(sb -> sb.regexp(m -> m.field("title").value(".*" + token + ".*")));

                // Boosting Query - positive gives better score, negative lowers score
                // Matches documents with boosted relevance in "title" field
                b.should(sb -> sb.boosting(bq -> bq.positive(m -> m.match(ma -> ma.field("name").query(token)))
                        .negative(m -> m.match(ma -> ma.field("description").query(token)))
                        .negativeBoost(0.5f)));

                // Match Phrase Query - useful for exact-phrase search
                // Matches documents with exact phrase match in "title" field
                b.should(sb -> sb.matchPhrase(m -> m.field("name").query(token)));

                // Fuzzy Query - similar to Match Query with fuzziness, useful for spelling errors
                // Matches documents with fuzzy matching in "title" field
                b.should(sb -> sb.match(
                        m -> m.field("name").fuzziness(Fuzziness.ONE.asString()).query(token)));

                // Range query - not applicable for dummy index, searches in the range from-to

                // More Like This query - finds documents similar to the provided text
                b.should(sb -> sb.moreLikeThis(mlt -> mlt
                    .fields("name")
                    .like(like -> like.text(token))
                    .minTermFreq(1)
                    .minDocFreq(1)));

                b.should(sb -> sb.moreLikeThis(mlt ->
                        mlt.fields("description")
                                .like(like -> like.text(token))
                                .minTermFreq(1)
                                .minDocFreq(1)));

                b.should(sb -> sb.moreLikeThis(mlt ->
                        mlt.fields("content_sr").like(
                                like -> like.text(token)
                        ).minTermFreq(1)));
            });
            return b;
        })))._toQuery();
    }

    private Query buildAdvancedSearchQuery(List<String> operands, String operation) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            var field1 = operands.get(0).split(":")[0];
            var value1 = operands.get(0).split(":")[1];
            var field2 = operands.get(1).split(":")[0];
            var value2 = operands.get(1).split(":")[1];

            switch (operation) {
                case "AND":
                    b.must(sb -> sb.match(
                            m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
                    b.must(sb -> sb.match(m -> m.field(field2).query(value2)));
                    break;
                case "OR":
                    b.should(sb -> sb.match(
                            m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
                    b.should(sb -> sb.match(m -> m.field(field2).query(value2)));
                    break;
                case "NOT":
                    b.must(sb -> sb.match(
                            m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
                    b.mustNot(sb -> sb.match(m -> m.field(field2).query(value2)));
                    break;
            }
            return b;
        })))._toQuery();
    }

    private Page<FacilityIndex> runQuery(NativeQuery searchQuery) {

        var searchHits = elasticsearchTemplate.search(searchQuery, FacilityIndex.class,
                IndexCoordinates.of("dummy_index"));

        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());

        return (Page<FacilityIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }
}
