package com.search.teacher.Techlearner.controller.test;

import lombok.Getter;

@Getter
public class ReqParam {
    private int count;
    private String gender;
    private String seed;
    private String format;

    public ReqParam(int count, String gender, String seed) {
        this.count = count;
        this.gender = gender;
        this.seed = seed;
    }

    public ReqParam(int count, String gender, String seed, String format) {
        this.count = count;
        this.gender = gender;
        this.seed = seed;
        this.format = format;
    }
}
