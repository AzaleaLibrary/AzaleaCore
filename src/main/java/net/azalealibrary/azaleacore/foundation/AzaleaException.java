package net.azalealibrary.azaleacore.foundation;

import java.util.Arrays;

public class AzaleaException extends RuntimeException {

    private final String[] messages;

    public AzaleaException(String message, String... messages) {
        super(message);
        this.messages = Arrays.stream(messages).filter(m -> !m.equals("")).toList().toArray(new String[0]);
    }

    public String[] getMessages() {
        return messages;
    }
}
