package com.search.teacher.Techlearner.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequest {
    private double rating;
    private Long teacherId;
}
