package com.azalealibrary.azaleacore.command.core;

public class CommandDescriptor {

    private final String name;
    private final String description;
    private final String usage;
    private final String[] aliases;
    private final String permission;
    private final String permissionMessage;

    public CommandDescriptor(String name, String description, String usage, String[] aliases, String permission, String permissionMessage) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getPermission() {
        return permission;
    }

    public String getPermissionMessage() {
        return permissionMessage;
    }
}
