package com.renault.garagesapi.exception;

public class AccessoryAlreadyAssignedException extends RuntimeException {

    public AccessoryAlreadyAssignedException() {
	super();
    }
    public AccessoryAlreadyAssignedException(String message) {
	super(message);
    }
    public AccessoryAlreadyAssignedException(String message, Throwable cause) {
	super(message, cause);
    }
    public AccessoryAlreadyAssignedException(Throwable cause) {
	super(cause);
    }
}
