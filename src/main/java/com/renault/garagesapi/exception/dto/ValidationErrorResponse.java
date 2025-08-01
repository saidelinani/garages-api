package com.renault.garagesapi.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        String message,
        int status,
        String error,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        String path,
        Map<String, String> fieldErrors
) {

    public static ValidationErrorResponse of(String message, String path, Map<String, String> fieldErrors) {
        return new ValidationErrorResponse(
                message,
                400,
                "Bad Request",
                LocalDateTime.now(),
                path,
                fieldErrors
        );
    }
}
