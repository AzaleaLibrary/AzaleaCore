package com.azalealibrary.azaleacore.api.core;

import com.azalealibrary.azaleacore.foundation.configuration.Property;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

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

    public void set(Player player, String[] params) {
        super.set(parser.parse(player, params, get()));
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

    public static <P> Builder<P> create(String name, @Nonnull P def) {
        return new Builder<>(name, def);
    }

    public static Builder<Integer> integer(String name, int def) {
        return ConfigurableProperty.create(name, def)
                .parse((p, s, v) -> Integer.valueOf(s[0]))
                .suggest((p, v) -> List.of(String.valueOf(v)));
    }

    public static Builder<Double> decimal(String name, double def) {
        return ConfigurableProperty.create(name, def)
                .parse((p, s, v) -> Double.valueOf(s[0]))
                .suggest((p, v) -> List.of(String.valueOf(v)));
    }

    public static Builder<Boolean> bool(String name, boolean def) {
        return ConfigurableProperty.create(name, def)
                .parse((p, s, v) -> Boolean.valueOf(s[0]))
                .suggest((p, v) -> List.of(String.valueOf(v)));
    }

    public static Builder<Location> location(String name, Location def) {
        return ConfigurableProperty.create(name, def)
                .parse((p, s, v) -> new Location(p.getWorld(), Double.parseDouble(s[0]), Double.parseDouble(s[1]), Double.parseDouble(s[2])))
                .suggest((p, v) -> List.of(p.getLocation().getBlockX() + " " + p.getLocation().getBlockY() + " " + p.getLocation().getBlockZ()));
    }

    public static Builder<List<Location>> locations(String name, List<Location> def) {
        return ConfigurableProperty.create(name, def)
                .parse((p, s, v) -> {
                    int index = Integer.parseInt(s[0].replace("@", ""));
                    if (s[1].equalsIgnoreCase("remove")) v.remove(index);
                    else v.add(index, new Location(p.getWorld(), Double.parseDouble(s[2]), Double.parseDouble(s[3]), Double.parseDouble(s[4])));
                    return v;
                })
                .suggest((p, v) -> Collections.singletonList("@" + v.size() + " add " + p.getLocation().getBlockX() + " " + p.getLocation().getBlockY() + " " + p.getLocation().getBlockZ()));
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
            this.parse = (p, s, v) -> (V) s;
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
    private interface Parser<P> {
        P parse(Player player, String[] params, P value);
    }

    @FunctionalInterface
    private interface Completer<P> {
        List<String> complete(Player player, P value);
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
