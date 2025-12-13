package com.secure_use_api.exceptions;

public class Exceptions {
    public static class UserAlreadyExists extends RuntimeException {
        public UserAlreadyExists(String message) {
            super(message);
        }
    }

    public static class InvalidException extends RuntimeException {
        public InvalidException(String message) {
            super(message);
        }
    }

    public static class ExpiredException extends RuntimeException {
        public ExpiredException(String message) {
            super(message);
        }
    }
}