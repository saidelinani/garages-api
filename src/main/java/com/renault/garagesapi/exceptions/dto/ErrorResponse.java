package com.renault.garagesapi.exceptions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

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
