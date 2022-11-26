package com.azalealibrary.azaleacore.foundation.registry;

import java.util.Objects;

public class AzaleaIdentifier {

    private final String namespace;
    private final String identifier;

    public AzaleaIdentifier(String name) {
        String[] inputs = name.split(":");

        if (inputs.length == 1) {
            throw new IllegalArgumentException("Invalid identifier provided '" + name + "'.");
        }

        this.namespace = inputs[0];
        this.identifier = inputs[1];
    }

    public AzaleaIdentifier(String namespace, String identifier) {
        this.namespace = namespace;
        this.identifier = identifier;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return namespace + ":" + identifier;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AzaleaIdentifier azaleaIdentifier) {
            return Objects.equals(azaleaIdentifier.toString(), toString());
        }
        return super.equals(object);
    }
}
