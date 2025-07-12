package ru.smirnov.keeneyepractice.backend.exceptions;

public class ServiceMethodNotImplementedException extends RuntimeException {

    public ServiceMethodNotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }

}
