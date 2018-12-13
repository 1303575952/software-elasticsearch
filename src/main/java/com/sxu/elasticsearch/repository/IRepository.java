package com.sxu.elasticsearch.repository;

import com.sxu.elasticsearch.entity.Software;
import com.sxu.elasticsearch.entity.Page;
import com.sxu.elasticsearch.entity.QueryDTO;

public interface IRepository {
    boolean save(Software Software);

    Page<Software> query(String queryString, int pageNo, int size);

    Page<Software> query(QueryDTO queryDTO, int pageNo, int size);

    Software get(String id);

}
