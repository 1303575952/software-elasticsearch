package com.sxu.elasticsearch.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryDTO {

    private String origin;

    private String category;

    private Float minId;

    private String orderBy;

}