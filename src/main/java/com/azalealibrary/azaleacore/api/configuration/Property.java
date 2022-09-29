package com.azalealibrary.azaleacore.api.configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class Property<V> implements Supplier<V> {

    private final String name;
    private final @Nonnull V def;
    private @Nullable V value;

    public Property(String name, @Nonnull V def) {
        this.name = name;
        this.def = def;
    }

    public String getName() {
        return name;
    }

    public V getDefault() {
        return def;
    }

    @Override
    public @Nonnull V get() {
        return Optional.ofNullable(value).orElse(def);
    }

    public void set(Object obj) {
        value = (V) obj;
    }

    public void reset() {
        value = def;
    }
}
