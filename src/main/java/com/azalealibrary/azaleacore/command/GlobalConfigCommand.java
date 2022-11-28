package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;

@AzaCommand(name = "!azalea")
public class GlobalConfigCommand extends AzaleaCommand { // TODO - review, merge with ConfigurationCommand?

    private static final String SET = "set";
    private static final String RESET = "reset";
    private static final String INFO = "info";

    public GlobalConfigCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> AzaleaConfiguration.getInstance().getProperties().stream().map(ConfigurableProperty::getName).toList());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2, (sender, arguments) -> List.of(SET, RESET, INFO));
        configurator.completeWhen((sender, arguments) -> arguments.is(1, SET), (sender, arguments) -> {
            Optional<ConfigurableProperty<?>> property = AzaleaConfiguration.getInstance().getProperties().stream().filter(p -> arguments.is(0, p.getName())).findFirst();
            return property.isPresent() ? property.get().suggest(sender, arguments.subArguments(2)) : List.of();
        });
        configurator.executeWhen((sender, arguments) -> arguments.size() >= 2, this::execute);
    }

    private Message execute(CommandSender sender, Arguments arguments) {
        ConfigurableProperty<?> property = arguments.find(0, "property", input -> AzaleaConfiguration.getInstance().getProperties().stream().filter(p -> p.getName().equals(input)).findFirst().orElse(null));
        String action = arguments.matchesAny(1, "action", SET, RESET, INFO);

        if (action.equals(SET)) {
            property.fromCommand(sender, arguments.subArguments(2));
            return ChatMessage.info("Global property '" + property.getName() + "' updated.");
        } else if (action.equals(RESET)) {
            property.reset();
            return ChatMessage.info("Global property '" + property.getName() + "' has been reset.");
        } else {
            return ChatMessage.info(property.toString());
        }
    }
}
