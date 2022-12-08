package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.manager.PlaygroundManager;
import com.azalealibrary.azaleacore.playground.Playground;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GotoCommand extends CommandNode {

    public GotoCommand() {
        super("@goto", new Lobby(), new Playgraund());
    }

    private static final class Lobby extends CommandNode {

        public Lobby() {
            super("lobby");
        }

        @Override
        public void execute(CommandSender sender, Arguments arguments) {
            if (sender instanceof Player player) {
                Playground playground = PlaygroundManager.getInstance().get(player);

                if (playground != null) {
                    playground.removePlayer(player);
                } else {
                    player.teleport(AzaleaConfiguration.getInstance().getServerLobby().getSpawnLocation().clone().add(.5, 0, .5));
                }
            }
        }
    }

    private static final class Playgraund extends CommandNode {

        public Playgraund() {
            super("playground");
        }

        @Override
        public List<String> complete(CommandSender sender, Arguments arguments) {
            return arguments.size() == 1 ? PlaygroundManager.getInstance().getKeys() : new ArrayList<>();
        }

        @Override
        public void execute(CommandSender sender, Arguments arguments) {
            if (sender instanceof Player player) {
                Playground playground = arguments.find(0, "playground", PlaygroundManager.getInstance()::get);
                playground.addPlayer(player);
            }
        }
    }
}
