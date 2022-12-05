package com.azalealibrary.azaleacore.command.__;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CommandNode extends Command {

    private final List<CommandNode> children;

    public CommandNode(String name, CommandNode... children) {
        super(name);
        this.children = List.of(children);
    }

    public List<CommandNode> getChildren() {
        return children;
    }

    @Override
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String label, String[] args) {
        execute(sender, new Arguments(this, List.of(args)));
        return true;
    }

    @Override
    public @Nonnull List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String label, String[] args) {
        return complete(sender, new Arguments(this, List.of(args)));
    }

    public void execute(CommandSender sender, Arguments arguments) {
        throw new AzaleaException("Invalid " + getName() + " command issued. Should be:", getUsage());
    }

    public List<String> complete(CommandSender sender, Arguments arguments) {
        if (!children.isEmpty()) {
            return children.stream()
                    .filter(n -> n.getPermission() == null || sender.hasPermission(n.getPermission()))
                    .map(Command::getName).toList();
        }
        return new ArrayList<>();
    }
}
