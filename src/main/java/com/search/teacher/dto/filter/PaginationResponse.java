package com.search.teacher.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationResponse {
    private int totalPages;
    private long totalSizes;
    private int currentSize;
    private int currentPage;
    private Object data;
}
