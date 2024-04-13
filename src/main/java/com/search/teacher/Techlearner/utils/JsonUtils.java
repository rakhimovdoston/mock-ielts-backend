package com.search.teacher.Techlearner.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JsonUtils {

    public static <T> List<T> convertFromJsonToList(String json, TypeReference<List<T>> var) throws IOException {
        return new ObjectMapper().readValue(json, var);
    }
}
