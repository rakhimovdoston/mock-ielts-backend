package com.search.teacher.Techlearner.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationResponse {
    private int totalPages;
    private int totalSizes;
    private int currentSize;
    private int currentPage;
    private Object data;
}
