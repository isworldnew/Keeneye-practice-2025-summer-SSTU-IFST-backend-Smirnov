package ru.smirnov.keeneyepractice.backend.exceptions;

public class NoSuchRoleException extends RuntimeException {

    public NoSuchRoleException(String message) {
        super(message);
    }

}
