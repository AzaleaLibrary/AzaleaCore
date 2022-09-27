package com.azalealibrary.azaleacore.api.configuration;

import com.azalealibrary.azaleacore.serialization.Serializable;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class CommandProperty<V> extends Property<V> implements Serializable {

    private final CommandPropertyParser<V> parser;
    private final CommandPropertyCompleter<V> completer;
    private final @Nullable Function<V, Object> serializer;
    private final @Nullable Function<Object, V> deserializer;

    protected CommandProperty(String name, @Nonnull V def, CommandPropertyParser<V> parser, CommandPropertyCompleter<V> completer, @Nullable Function<V, Object> serializer, @Nullable Function<Object, V> deserializer) {
        super(name, def);
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
        configuration.set(getName(), serializer != null ? serializer.apply(get()) : get());
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        set(deserializer != null ? deserializer.apply(configuration.get(getName())) : configuration.get(getName()));
    }

    public static <P> Builder<P> create(String name, @Nonnull P def) {
        return new Builder<>(name, def);
    }

    public static Builder<Integer> integer(String name, int def) {
        return CommandProperty.create(name, def)
                .parse((p, s, v) -> Integer.valueOf(s[0]))
                .suggest((p, v) -> List.of(ChatColor.YELLOW + String.valueOf(v) + ChatColor.RESET));
    }

    public static Builder<Double> decimal(String name, double def) {
        return CommandProperty.create(name, def)
                .parse((p, s, v) -> Double.valueOf(s[0]))
                .suggest((p, v) -> List.of(ChatColor.YELLOW + String.valueOf(v) + ChatColor.RESET));
    }

    public static Builder<Boolean> bool(String name, boolean def) {
        return CommandProperty.create(name, def)
                .parse((p, s, v) -> Boolean.valueOf(s[0]))
                .suggest((p, v) -> List.of((v ? ChatColor.GREEN : ChatColor.RED) + String.valueOf(v) + ChatColor.RESET));
    }

    public static Builder<Location> location(String name, Location def) {
        return CommandProperty.create(name, def)
                .parse((p, s, v) -> new Location(p.getWorld(), Double.parseDouble(s[0]), Double.parseDouble(s[1]), Double.parseDouble(s[2])))
                .suggest((p, v) -> List.of(ChatColor.AQUA.toString() + p.getLocation().getBlockX() + " " + p.getLocation().getBlockY() + " " + p.getLocation().getBlockZ() + ChatColor.RESET));
    }

    public static Builder<List<Location>> locations(String name, List<Location> def) {
        return CommandProperty.create(name, def)
                .parse((p, s, v) -> {
                    int index = Integer.parseInt(s[0].replace("@", ""));
                    if (s[1].equalsIgnoreCase("remove")) v.remove(index);
                    else v.add(index, new Location(p.getWorld(), Double.parseDouble(s[2]), Double.parseDouble(s[3]), Double.parseDouble(s[4])));
                    return v;
                })
                .suggest((p, v) -> Collections.singletonList(ChatColor.YELLOW + "@" + v.size() + ChatColor.AQUA + " add " + ChatColor.AQUA + p.getLocation().getBlockX() + " " + p.getLocation().getBlockY() + " " + p.getLocation().getBlockZ() + ChatColor.RESET));
    }

    @SuppressWarnings("unchecked")
    public static class Builder<P> {

        private final String name;
        private final P def;
        private CommandPropertyParser<P> parse;
        private CommandPropertyCompleter<P> completer;
        private @Nullable Function<P, Object> serializer;
        private @Nullable Function<Object, P> deserializer;

        private Builder(String name, P def) {
            this.name = name;
            this.def = def;
            this.parse = (p, s, v) -> (P) s;
            this.completer = (p, v) -> List.of(def.toString());
        }

        public Builder<P> parse(CommandPropertyParser<P> parse) {
            this.parse = parse;
            return this;
        }

        public Builder<P> suggest(CommandPropertyCompleter<P> completer) {
            this.completer = completer;
            return this;
        }

        public Builder<P> serialize(Function<P, Object> serializer) {
            this.serializer = serializer;
            return this;
        }

        public Builder<P> deserialize(Function<Object, P> deserializer) {
            this.deserializer = deserializer;
            return this;
        }

        public CommandProperty<P> build() {
            return new CommandProperty<>(name, def, parse, completer, serializer, deserializer);
        }
    }

    @FunctionalInterface
    public interface CommandPropertyParser<P> {
        P parse(Player player, String[] params, P value);
    }

    @FunctionalInterface
    public interface CommandPropertyCompleter<P> {
        List<String> complete(Player player, P value);
    }
}
