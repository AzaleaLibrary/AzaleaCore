package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class PropertyType<T> {

    public static final PropertyType<String> STRING = new Builder<String>()
            .parse((sender, arguments, currentValue) -> arguments.getLast())
            .complete((sender, arguments, currentValue) -> List.of("<text>"))
            .done();
    public static final PropertyType<Integer> INTEGER = new Builder<Integer>()
            .parse((sender, arguments, currentValue) -> Integer.parseInt(arguments.getLast()))
            .complete((sender, arguments, currentValue) -> List.of("<number>"))
            .done();
    public static final PropertyType<Boolean> BOOLEAN = new Builder<Boolean>()
            .parse((sender, arguments, currentValue) -> Boolean.parseBoolean(arguments.getLast()))
            .complete((sender, arguments, currentValue) -> List.of(Boolean.toString(Boolean.FALSE.equals(currentValue))))
            .done();
    public static final PropertyType<Vector> VECTOR = new Builder<Vector>()
            .parse((sender, arguments, currentValue) -> {
                double x = arguments.find(0, "x", Double::parseDouble);
                double y = arguments.find(1, "y", Double::parseDouble);
                double z = arguments.find(2, "z", Double::parseDouble);
                return new Vector(x, y, z);
            })
            .complete((sender, arguments, currentValue) -> {
                if (sender instanceof Player player) {
                    Location location = player.getLocation();
                    double x = location.getBlockX() + .5;
                    double y = location.getBlockY() + .5;
                    double z = location.getBlockZ() + .5;
                    return List.of(x + " " + y + " " + z);
                }
                return List.of();
            })
            .done();
    public static final PropertyType<Player> PLAYER = new Builder<Player>()
            .parse((sender, arguments, currentValue) -> (Player) sender)
            .complete((sender, arguments, currentValue) -> {
                if (sender instanceof Player player) {
                    return player.getWorld().getPlayers().stream().map(Player::getDisplayName).toList();
                }
                return List.of();
            })
            .print(Player::getDisplayName)
            .toObject(player -> player.getUniqueId().toString())
            .toValue(uuid -> Bukkit.getPlayer(UUID.fromString((String) uuid)))
            .done();

    private final Parser<T> parser;
    private final Completer<T> completer;
    private final Function<T, String> toString;
    private final Function<T, Object> toObject;
    private final Function<Object, T> toValue;

    private PropertyType(Parser<T> parser, Completer<T> completer, Function<T, String> toString, Function<T, Object> toObject, Function<Object, T> toValue) {
        this.parser = parser;
        this.completer = completer;
        this.toString = toString;
        this.toObject = toObject;
        this.toValue = toValue;
    }

    public T parse(CommandSender sender, Arguments arguments, @Nullable T currentValue) {
        try {
            return parser.parse(sender, arguments, currentValue);
        } catch (Exception exception) {
            throw new AzaleaException("Invalid arguments provided :" + arguments + ".");
        }
    }

    public List<String> suggest(CommandSender sender, Arguments arguments, @Nullable T currentValue) {
        return completer.complete(sender, arguments, currentValue);
    }

    public String toString(T value) {
        return toString.apply(value);
    }

    public Object toObject(T value) {
        return toObject.apply(value);
    }

    public T toValue(Object object) {
        return toValue.apply(object);
    }

    @FunctionalInterface
    public interface Parser<T> {
        T parse(CommandSender sender, Arguments arguments, @Nullable T currentValue);
    }

    @FunctionalInterface
    public interface Completer<T> {
        List<String> complete(CommandSender sender, Arguments arguments, @Nullable T currentValue);
    }

    public static class Builder<T> {

        private Parser<T> parser = (sender, arguments, currentValue) -> (T) arguments.getLast();
        private Completer<T> completer = (sender, arguments, currentValue) -> List.of();
        private Function<T, String> toString = Object::toString;
        private Function<T, Object> toObject = value -> value;
        private Function<Object, T> toValue = object -> (T) object;

        public Builder<T> parse(Parser<T> parser) {
            this.parser = parser;
            return this;
        }

        public Builder<T> complete(Completer<T> completer) {
            this.completer = completer;
            return this;
        }

        public Builder<T> print(Function<T, String> function) {
            this.toString = function;
            return this;
        }

        public Builder<T> toObject(Function<T, Object> function) {
            this.toObject = function;
            return this;
        }

        public Builder<T> toValue(Function<Object, T> function) {
            this.toValue = function;
            return this;
        }

        public PropertyType<T> done() {
            return new PropertyType<>(parser, completer, toString, toObject, toValue);
        }
    }
}
