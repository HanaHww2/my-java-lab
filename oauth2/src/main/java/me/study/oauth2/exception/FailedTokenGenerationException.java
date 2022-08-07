package me.study.oauth2.exception;

public class FailedTokenGenerationException extends RuntimeException {

    public FailedTokenGenerationException() {
        super("Failed to generate Token.");
    }

    private FailedTokenGenerationException(String message) {
        super(message);
    }
}
