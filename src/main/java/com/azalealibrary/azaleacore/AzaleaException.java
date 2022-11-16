package com.azalealibrary.azaleacore;

public class AzaleaException extends RuntimeException {

    private final String[] messages;

    public AzaleaException(String message, String... messages) {
        super(message);
        this.messages = messages;
    }

    public String[] getMessages() {
        return messages;
    }
}
