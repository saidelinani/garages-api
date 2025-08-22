package com.renault.garagesapi.exceptions;

public class GarageQuotaReachedException extends RuntimeException {

    public GarageQuotaReachedException() {
        super();
    }
    public GarageQuotaReachedException(String message) {
        super(message);
    }
    public GarageQuotaReachedException(String message, Throwable cause) {
        super(message, cause);
    }
    public GarageQuotaReachedException(Throwable cause) {
        super(cause);
    }
}
