package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.util.TextUtil;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class ConfigureCommand extends CommandNode {

    private static final String SET = "set";
    private static final String RESET = "reset";
    private static final String INFO = "info";

    public ConfigureCommand() {
        super("configure");
    }

    @Override
    public List<String> complete(CommandSender sender, Arguments arguments) {
        if (arguments.size() == 1) {
            return completeConfigurable(sender, arguments);
        } else {
            Configurable configurable = arguments.find(0, "configurable", this::getConfigurable);
            List<ConfigurableProperty<?>> properties = configurable.getProperties();

            if (arguments.size() == 2) {
                return properties.stream().map(ConfigurableProperty::getName).toList();
            } else if (arguments.size() == 3) {
                return List.of(SET, RESET, INFO);
            } else if (arguments.size() > 3 && arguments.is(2, SET)) {
                return properties.stream()
                        .filter(p -> p.getName().equals(arguments.get(1)))
                        .findFirst().map(p -> p.suggest(sender, arguments.subArguments(3)))
                        .orElse(List.of());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        Configurable configurable = arguments.find(0, "configurable", this::getConfigurable);
        ConfigurableProperty<?> property = arguments.find(1, "property", input -> configurable.getProperties().stream().filter(p -> p.getName().equals(input)).findFirst().orElse(null));
        String action = arguments.matchesAny(2, "action", SET, RESET, INFO);

        switch (action) {
            case SET -> {
                property.fromCommand(sender, arguments.subArguments(3));
                ChatMessage.info("Property " + TextUtil.getName(property) + " updated.").post(AzaleaCore.PLUGIN_ID, sender);
            }
            case RESET -> {
                property.reset();
                ChatMessage.info("Property " + TextUtil.getName(property) + " has been reset.").post(AzaleaCore.PLUGIN_ID, sender);
            }
            case INFO -> ChatMessage.info(TextUtil.getString(property)).post(AzaleaCore.PLUGIN_ID, sender);
        }
    }

    protected abstract List<String> completeConfigurable(CommandSender sender, Arguments arguments);

    protected abstract @Nullable Configurable getConfigurable(String input);
}
