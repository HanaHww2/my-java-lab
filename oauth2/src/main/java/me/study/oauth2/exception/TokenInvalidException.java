package me.study.oauth2.exception;

public class TokenInvalidException extends RuntimeException {

    public TokenInvalidException() {
        super("Failed to generate Token.");
    }

    private TokenInvalidException(String message) {
        super(message);
    }
}
