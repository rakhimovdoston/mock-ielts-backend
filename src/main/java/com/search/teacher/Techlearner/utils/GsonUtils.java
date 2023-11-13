package com.search.teacher.Techlearner.utils;

import com.google.gson.Gson;
import com.search.teacher.Techlearner.model.response.JResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class GsonUtils {

    public static String toJsonString(JResponse response) {
        Gson gson = new Gson();
        return gson.toJson(response);
    }

    public static void responseError(int status, String message, HttpServletResponse response) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(toJsonString(JResponse.error(status, message)));
        printWriter.flush();
    }
}
