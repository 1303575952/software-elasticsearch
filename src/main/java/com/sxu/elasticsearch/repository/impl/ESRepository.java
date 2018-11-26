package com.sxu.elasticsearch.repository.impl;

import com.sxu.elasticsearch.entity.Movie;
import com.sxu.elasticsearch.entity.Page;
import com.sxu.elasticsearch.entity.QueryDTO;
import com.sxu.elasticsearch.repository.IRepository;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class ESRepository implements IRepository {

    public static final String INDEX = "indextest";
    public static final String TYPE = "typetest";

    @Autowired
    private JestClient client;

    @Override
    public boolean save(Movie movie) {
        Index index = new Index.Builder(movie).index(INDEX).type(TYPE).build();
        try {
            JestResult jestResult = client.execute(index);
            log.info("save返回结果{}", jestResult.getJsonString());
            return true;
        } catch (IOException e) {
            log.error("save异常", e);
            return false;
        }
    }

    @Override
    public Page<Movie> query(String queryString, int pageNo, int size) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false).tagsSchema("default");
        searchSourceBuilder.highlighter(highlightBuilder);
        QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(queryString);
        //field方法有多个
        queryStringQueryBuilder.field("software")
                .field("copyright");
        searchSourceBuilder.query(queryStringQueryBuilder).from(from(pageNo, size)).size(size);
        log.debug("搜索DSL:{}", searchSourceBuilder.toString());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(INDEX)
                .addType(TYPE)
                .build();
        try {
            SearchResult result = client.execute(search);
            List<SearchResult.Hit<Movie, Void>> hits = result.getHits(Movie.class);
            List<Movie> movies = new ArrayList<>();
            hits.forEach(hit -> {
                Movie movie = hit.source;
                Map<String, List<String>> highlight = hit.highlight;
                if (highlight.containsKey("software")) {
                    movie.setSoftware(highlight.get("software").get(0));
                }
                if (highlight.containsKey("copyright")) {
                    movie.setCopyright(highlight.get("copyright").get(0));
                }
                movies.add(movie);
            });
            int took = result.getJsonObject().get("took").getAsInt();
            Page<Movie> page = Page.<Movie>builder().list(movies).pageNo(pageNo).size(size).total(result.getTotal()).took(took).build();
            return page;
        } catch (IOException e) {
            log.error("search异常", e);
            return null;
        }
    }
    @Override
    public Page<Movie> query(QueryDTO queryDTO, int pageNo, int size) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().from(from(pageNo, size)).size(size);
        if (queryDTO.getMinId() != null) {
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("id").gte(queryDTO.getMinId());
            boolQueryBuilder.must(rangeQueryBuilder);
            searchSourceBuilder.query(boolQueryBuilder);
        }
        if (queryDTO.getOrderBy() != null) {
            searchSourceBuilder.sort(queryDTO.getOrderBy(), SortOrder.DESC);
        }
        log.debug("搜索DSL:{}", searchSourceBuilder.toString());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(INDEX)
                .addType(TYPE)
                .build();
        try {
            SearchResult result = client.execute(search);
            List<Movie> movies = result.getSourceAsObjectList(Movie.class, false);
            System.out.println("result:\n"+result.getJsonObject().toString());
            int took = result.getJsonObject().get("took").getAsInt();
            Page<Movie> page = Page.<Movie>builder().list(movies).pageNo(pageNo).size(size).total(result.getTotal()).took(took).build();
            return page;
        } catch (IOException e) {
            log.error("search异常", e);
            return null;
        }

    }
    @Override
    public Movie get(String id) {
        Get get = new Get.Builder(INDEX, id).type(TYPE).build();
        try {
            JestResult result = client.execute(get);
            Movie movie = result.getSourceAsObject(Movie.class);
            return movie;
        } catch (IOException e) {
            log.error("get异常", e);
            return null;
        }
    }

    private int from(int pageNo, int size) {
        return (pageNo - 1) * size < 0 ? 0 : (pageNo - 1) * size;
    }
}
