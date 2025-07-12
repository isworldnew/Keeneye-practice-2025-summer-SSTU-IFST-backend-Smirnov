package ru.smirnov.keeneyepractice.backend.exceptions;

public class ServiceMethodNotImplementedException extends RuntimeException {

    public ServiceMethodNotImplementedException(String message) {
        super(message);
    }

}
