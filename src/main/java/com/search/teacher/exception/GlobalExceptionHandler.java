package com.search.teacher.exception;

import com.search.teacher.model.response.JResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<JResponse> badRequest(BadRequestException e) {
        if (e.getResponse() == null)
            return new ResponseEntity<>(JResponse.error(404, e.getMessage()), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(e.getResponse(), HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<JResponse> exception(Exception e) {
        e.printStackTrace();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "An error occurred while processing the request.";

        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            status = HttpStatus.FORBIDDEN;
            message = e.getMessage();
        }
        return ResponseEntity.status(status)
                .body(JResponse.error(status.value(), message));
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<String> handleInvalidFileTypeException(InvalidFileTypeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
