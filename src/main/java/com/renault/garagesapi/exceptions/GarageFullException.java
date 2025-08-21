package com.renault.garagesapi.exceptions;

public class GarageFullException extends RuntimeException {

    public GarageFullException() {
        super();
    }
    public GarageFullException(String message) {
        super(message);
    }
    public GarageFullException(String message, Throwable cause) {
        super(message, cause);
    }
    public GarageFullException(Throwable cause) {
        super(cause);
    }
}
