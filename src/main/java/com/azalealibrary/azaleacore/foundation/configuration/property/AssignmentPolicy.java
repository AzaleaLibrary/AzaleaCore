package com.azalealibrary.azaleacore.foundation.configuration.property;

import org.bukkit.ChatColor;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class AssignmentPolicy<T> {

    public static final AssignmentPolicy<Integer> POSITIVE_INTEGER = AssignmentPolicy.create(value -> value > 0, "Number must be positive.");

    private final String message;
    private final Predicate<T> predicate;

    private AssignmentPolicy(String message, Predicate<T> predicate) {
        this.message = message;
        this.predicate = predicate;
    }

    public String getMessage(T value) {
        return String.format(ChatColor.RED + message, value);
    }

    public boolean canAssign(@Nullable T value) {
        return predicate.test(value);
    }

    public static <T> AssignmentPolicy<T> create(Predicate<T> predicate) {
        return create(predicate, "Can not assign property with value '%s'.");
    }

    public static <T> AssignmentPolicy<T> create(Predicate<T> predicate, String message) {
        return new AssignmentPolicy<>(message, predicate);
    }
}
