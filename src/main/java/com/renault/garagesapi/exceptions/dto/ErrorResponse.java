package com.renault.garagesapi.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        String message,
        int status,
        String error,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        String path
) {
    public static ErrorResponse of(String message, int status, String error, String path) {
        return new ErrorResponse(message, status, error, LocalDateTime.now(), path);
    }
}
