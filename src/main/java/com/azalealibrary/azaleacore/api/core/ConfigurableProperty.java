package com.azalealibrary.azaleacore.api.core;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.configuration.Property;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ConfigurableProperty<V> extends Property<V> implements Serializable {

    private final Parser<V> parser;
    private final Completer<V> completer;
    private final @Nullable Serializer<V> serializer;
    private final @Nullable Deserializer<V> deserializer;

    private ConfigurableProperty(String name, @Nonnull V defaultValue, Parser<V> parser, Completer<V> completer, @Nullable Serializer<V> serializer, @Nullable Deserializer<V> deserializer) {
        super(name, defaultValue);
        this.parser = parser;
        this.completer = completer;
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public void set(Player player, Arguments arguments) {
        super.set(parser.parse(player, arguments, get()));
    }

    public List<String> suggest(Player player) {
        return completer.complete(player, get());
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        configuration.set(getConfigName(), serializer != null ? serializer.serialize(get()) : get());
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        set(deserializer != null ? deserializer.deserialize(configuration.get(getConfigName())) : configuration.get(getConfigName()));
    }

    public static <P> Builder<P> create(String name, @Nonnull P defaultValue) {
        return new Builder<>(name, defaultValue);
    }

    public static Builder<Integer> integer(String name, int defaultValue) {
        return ConfigurableProperty.create(name, defaultValue)
                .suggest((p, v) -> List.of(String.valueOf(v)))
                .parse((p, a, v) -> Integer.valueOf(a.get(0)));
    }

    public static Builder<Double> decimal(String name, double defaultValue) {
        return ConfigurableProperty.create(name, defaultValue)
                .suggest((p, v) -> List.of(String.valueOf(v)))
                .parse((p, a, v) -> Double.valueOf(a.get(0)));
    }

    public static Builder<Boolean> bool(String name, boolean defaultValue) {
        return ConfigurableProperty.create(name, defaultValue)
                .suggest((p, v) -> List.of(String.valueOf(v)))
                .parse((p, a, v) -> Boolean.valueOf(a.get(0)));
    }

    public static Builder<Vector> location(String name, Vector defaultValue) {
        return ConfigurableProperty.create(name, defaultValue)
                .suggest((p, v) -> List.of(p.getLocation().getBlockX() + " " + p.getLocation().getBlockY() + " " + p.getLocation().getBlockZ()))
                .parse((p, a, v) -> new Vector(Double.parseDouble(a.get(0)) + .5, Double.parseDouble(a.get(1)) + .5, Double.parseDouble(a.get(2)) + .5));
    }

    public static Builder<List<Vector>> locations(String name, List<Vector> defaultValue) {
        return ConfigurableProperty.create(name, defaultValue)
                .suggest((p, v) -> Collections.singletonList("@" + v.size() + " add " + p.getLocation().getBlockX() + " " + p.getLocation().getBlockY() + " " + p.getLocation().getBlockZ()))
                .parse((p, a, v) -> {
                    int index = Integer.parseInt(a.get(0).replace("@", ""));
                    if (a.get(1).equalsIgnoreCase("remove")) v.remove(index);
                    else v.add(index, new Vector(Double.parseDouble(a.get(2)) + .5, Double.parseDouble(a.get(3)) + .5, Double.parseDouble(a.get(4)) + .5));
                    return v;
                });
    }

    public static class Builder<V> {

        private final String name;
        private final V defaultValue;
        private Parser<V> parse;
        private Completer<V> completer;
        private @Nullable Serializer<V> serializer;
        private @Nullable Deserializer<V> deserializer;

        private Builder(String name, V defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
            this.parse = (p, a, v) -> (V) a.get(0);
            this.completer = (p, v) -> List.of(defaultValue.toString());
        }

        public Builder<V> parse(Parser<V> parse) {
            this.parse = parse;
            return this;
        }

        public Builder<V> suggest(Completer<V> completer) {
            this.completer = completer;
            return this;
        }

        public Builder<V> serialize(Serializer<V> serializer) {
            this.serializer = serializer;
            return this;
        }

        public Builder<V> deserialize(Deserializer<V> deserializer) {
            this.deserializer = deserializer;
            return this;
        }

        public ConfigurableProperty<V> build() {
            return new ConfigurableProperty<>(name, defaultValue, parse, completer, serializer, deserializer);
        }
    }

    @FunctionalInterface
    private interface Parser<V> {
        V parse(Player player, Arguments arguments, V value);
    }

    @FunctionalInterface
    private interface Completer<V> {
        List<String> complete(Player player, V value);
    }

    @FunctionalInterface
    private interface Serializer<V> {
        Object serialize(V value);
    }

    @FunctionalInterface
    private interface Deserializer<V> {
        V deserialize(Object value);
    }
}
