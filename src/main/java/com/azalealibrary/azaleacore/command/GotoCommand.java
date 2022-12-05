package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.manager.PlaygroundManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GotoCommand extends AzaleaCommand {

    public GotoCommand() {
        super("@goto", new Lobby(), new Playground());
    }

    private static final class Lobby extends AzaleaCommand {

        public Lobby() {
            super("lobby");
        }

        @Override
        public void execute(CommandSender sender, Arguments arguments) {
            if (sender instanceof Player player) {
                player.teleport(AzaleaConfiguration.getInstance().getServerLobby().getSpawnLocation());
            }
        }
    }

    private static final class Playground extends AzaleaCommand {

        public Playground() {
            super("playground");
        }

        @Override
        public List<String> complete(CommandSender sender, Arguments arguments) {
            return arguments.size() == 1 ? PlaygroundManager.getInstance().getKeys() : new ArrayList<>();
        }

        @Override
        public void execute(CommandSender sender, Arguments arguments) {
            if (sender instanceof Player player) {
                com.azalealibrary.azaleacore.playground.Playground playground = arguments.find(0, "playground", PlaygroundManager.getInstance()::get);
                player.teleport(playground.getWorld().getSpawnLocation());
            }
        }
    }
}
