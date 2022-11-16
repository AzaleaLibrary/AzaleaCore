package com.azalealibrary.azaleacore.command.core;

public class AzaleaCommandException extends RuntimeException {

    private final String[] messages;

    public AzaleaCommandException(String message, String... messages) {
        super(message);
        this.messages = messages;
    }

    public String[] getMessages() {
        return messages;
    }
}
