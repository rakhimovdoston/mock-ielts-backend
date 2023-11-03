package com.search.teacher.Techlearner.exception;

import com.search.teacher.Techlearner.model.response.JResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotfoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<JResponse> notfound(NotfoundException e) {
        return new ResponseEntity<>(JResponse.error(404, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<JResponse> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> errors = new HashMap<>();
        for (ConstraintViolation<?> violation: ex.getConstraintViolations()) {
            errors.put(violation.getMessageTemplate(), violation.getInvalidValue());
        }
        return ResponseEntity.badRequest().body(JResponse.error(400, "badRequest", errors));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<JResponse> exception(Exception e) {
        return ResponseEntity.internalServerError().body(JResponse.error(500, e.getLocalizedMessage()));
    }
}
