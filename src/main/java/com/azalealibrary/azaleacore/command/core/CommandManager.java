package com.azalealibrary.azaleacore.command.core;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

public final class CommandManager {

    public static void register(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(AzaleaCommand.class)) {
            throw new IllegalArgumentException("Registering non-command class as Azalea command.");
        }

        AzaleaCommand annotation = clazz.getAnnotation(AzaleaCommand.class);
        String name = annotation.name();
        String description = annotation.description();
        String usage = annotation.usage();
        String[] aliases = annotation.aliases();
        String permission = annotation.permission();
        String permissionMessage = annotation.permissionMessage();

        CommandHandler.Builder builder = new CommandHandler.Builder(name)
                .setDescription(description)
                .setUsage(usage)
                .setAliases(aliases)
                .setPermission(permission)
                .setPermissionMessage(permissionMessage);

        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            clazz.getConstructor(CommandHandler.Builder.class).newInstance(builder);
            map.register(name, builder.build());
        } catch (Exception exception) {
            // TODO - better error handling
            System.err.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
