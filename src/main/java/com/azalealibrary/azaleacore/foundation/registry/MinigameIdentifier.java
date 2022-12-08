package com.azalealibrary.azaleacore.foundation.registry;

import com.azalealibrary.azaleacore.util.TextUtil;

import java.util.Objects;

public class MinigameIdentifier {

    private final String namespace;

    public MinigameIdentifier(String namespace) {
        this.namespace = TextUtil.ensureSimple(namespace, "name");
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

        public Tag(String name) {
            String[] input = name.split(":");

            if (input.length != 2) {
                throw new IllegalArgumentException("Invalid tag name provided '" + name + "'.");
            }

            this.identifier = new MinigameIdentifier(TextUtil.ensureSimple(input[0], "name"));
            this.name = TextUtil.ensureSimple(input[1], "name");
        }

        private Tag(MinigameIdentifier identifier, String name) {
            this.identifier = identifier;
            this.name = TextUtil.ensureSimple(name, "name");
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
}
