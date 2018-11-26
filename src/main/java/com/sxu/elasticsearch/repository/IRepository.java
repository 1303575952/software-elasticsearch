package com.sxu.elasticsearch.repository;

import com.sxu.elasticsearch.entity.Movie;
import com.sxu.elasticsearch.entity.Page;
import com.sxu.elasticsearch.entity.QueryDTO;

public interface IRepository {
    boolean save(Movie movie);

    Page<Movie> query(String queryString, int pageNo, int size);

    Page<Movie> query(QueryDTO queryDTO, int pageNo, int size);

    Movie get(String id);

}
