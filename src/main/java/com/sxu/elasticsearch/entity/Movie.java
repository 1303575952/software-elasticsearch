package com.sxu.elasticsearch.entity;

import io.searchbox.annotations.JestId;
import lombok.Data;

@Data
public class Movie {
    @JestId
    private Integer id;

    private Integer num;

    private String software;

    private String shortened_form;

    private String company;

    private String date;

    private String reg_pos;

    private String reg_num;

    private String copyright;
}
