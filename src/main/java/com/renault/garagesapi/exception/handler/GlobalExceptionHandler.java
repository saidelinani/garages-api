package com.renault.garagesapi.exception.handler;

import com.renault.garagesapi.exception.AccessoryAlreadyAssignedException;
import com.renault.garagesapi.exception.GarageFullException;
import com.renault.garagesapi.exception.ResourceNotFoundException;
import com.renault.garagesapi.exception.dto.ErrorResponse;
import com.renault.garagesapi.exception.dto.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                "Resource not found",
                getPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                "Erreurs de validation",
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                LocalDateTime.now(),
                getPath(request),
                fieldErrors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.of(
                "Une erreur interne s'est produite",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                getPath(request)
        );

        // Todo: to remove
        ex.printStackTrace();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessoryAlreadyAssignedException.class)
    public ResponseEntity<ErrorResponse> handleAccessoryAlreadyAssigned(
		    AccessoryAlreadyAssignedException ex,
		    WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.of(
                        ex.getMessage(),
                        HttpStatus.CONFLICT.value(),
                        "Conflict",
                        getPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(GarageFullException.class)
    public ResponseEntity<ErrorResponse> handleGarageFullException(
            GarageFullException ex,
            WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                getPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
