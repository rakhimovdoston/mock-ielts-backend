package com.search.teacher.Techlearner.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

public class JResponse {
    private int code;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object data;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> error;

    public JResponse(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public JResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public JResponse(int code, String message, Map<String, Object> error) {
        this.code = code;
        this.message = message;
        this.error = error;
    }

    public static JResponse success(String message) {
        return new JResponse(200, message);
    }

    public static JResponse success(Object data) {
        return new JResponse(200, data);
    }

    public static JResponse success() {
        return new JResponse(200, "success");
    }

    public static JResponse error(int code, String message, Map<String, Object> error) {
        return new JResponse(code, message, error);
    }

    public static JResponse error(int code, String message) {
        return new JResponse(code, message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Map<String, Object> getError() {
        return error;
    }

    public void setError(Map<String, Object> error) {
        this.error = error;
    }
}
