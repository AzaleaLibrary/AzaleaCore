package net.azalealibrary.core.command;

import net.azalealibrary.core.foundation.AzaleaConfiguration;
import net.azalealibrary.core.foundation.configuration.Configurable;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.List;

public class AzaleaCommand extends CommandNode {

    public AzaleaCommand() {
        super("@azalea", new ConfigureCommand() {
            @Override
            protected List<String> completeConfigurable(CommandSender sender, Arguments arguments) {
                return List.of("global");
            }

            @Override
            protected @Nullable Configurable getConfigurable(String input) {
                return AzaleaConfiguration.getInstance();
            }
        });
    }
}
