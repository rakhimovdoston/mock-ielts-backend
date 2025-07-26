package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "request_logs")
@Getter
@Setter
@Builder
public class RequestLog extends BaseEntity {
    private String url;
    private String method;
    @Column(columnDefinition = "TEXT")
    private String requestBody;
    @Column(columnDefinition = "TEXT")
    private String responseBody;
    private Long duration;
    private String status;
    private String error;

    public RequestLog() {}

    public RequestLog(String url, String method, String requestBody, String responseBody, Long duration, String status, String error) {
        this.url = url;
        this.method = method;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.duration = duration;
        this.status = status;
        this.error = error;
    }
}
