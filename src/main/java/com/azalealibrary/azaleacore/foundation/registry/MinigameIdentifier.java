package com.azalealibrary.azaleacore.foundation.registry;

import java.util.Objects;

public class MinigameIdentifier {

    private final String namespace;

    public MinigameIdentifier(String namespace) {
        this.namespace = verifyName(namespace);
    }

    public String getNamespace() {
        return namespace;
    }

    public MinigameIdentifier.Tag tag(String name) {
        return new MinigameIdentifier.Tag(this, name);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof MinigameIdentifier identifier) {
            return Objects.equals(namespace, identifier.namespace);
        }
        return super.equals(object);
    }

    @Override
    public String toString() {
        return namespace;
    }

    public static class Tag {

        private final MinigameIdentifier identifier;
        private final String name;

        private Tag(MinigameIdentifier identifier, String name) {
            this.identifier = identifier;
            this.name = verifyName(name);
        }

        public MinigameIdentifier getIdentifier() {
            return identifier;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof MinigameIdentifier.Tag tag) {
                return tag.identifier.equals(identifier) && Objects.equals(tag.name, name);
            }
            return super.equals(object);
        }

        @Override
        public String toString() {
            return identifier.namespace + ":" + name;
        }
    }

    private static String verifyName(String name) {
        if (!name.matches("^[a-zA-Z0-9-_]*$")) {
            throw new IllegalArgumentException("Invalid id name provided '" + name + "'. Name must be alphanumeric.");
        }
        return name;
    }
}
