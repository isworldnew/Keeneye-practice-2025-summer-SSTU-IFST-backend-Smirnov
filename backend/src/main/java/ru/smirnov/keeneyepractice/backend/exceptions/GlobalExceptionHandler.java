package ru.smirnov.keeneyepractice.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchRoleException.class)
    public ResponseEntity<String> handleNoSuchRoleException(NoSuchRoleException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ServiceMethodNotImplementedException.class)
    public ResponseEntity<String> handleServiceMethodNotImplementedException(ServiceMethodNotImplementedException ex) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(ex.getMessage());
    }
}
