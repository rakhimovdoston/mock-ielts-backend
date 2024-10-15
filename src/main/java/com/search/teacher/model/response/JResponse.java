package com.search.teacher.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class JResponse {
    private int code;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object data;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> error;

    public JResponse(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public JResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public JResponse(int code, String message, Map<String, String> error) {
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

    public static JResponse error(int code, String message, Map<String, String> error) {
        return new JResponse(code, message, error);
    }

    public static JResponse error(int code, String message) {
        return new JResponse(code, message);
    }

    public boolean isSuccess() {
        return code == 200;
    }

    public boolean isError() {
        return code != 200;
    }

}
