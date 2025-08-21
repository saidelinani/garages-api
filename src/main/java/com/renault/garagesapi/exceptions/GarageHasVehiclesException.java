package com.renault.garagesapi.exceptions;

public class GarageHasVehiclesException extends RuntimeException {

    public GarageHasVehiclesException() {
        super();
    }
    public GarageHasVehiclesException(String message) {
        super(message);
    }
    public GarageHasVehiclesException(String message, Throwable cause) {
        super(message, cause);
    }
    public GarageHasVehiclesException(Throwable cause) {
        super(cause);
    }
}
