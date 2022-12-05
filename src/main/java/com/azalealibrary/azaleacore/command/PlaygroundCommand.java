package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.registry.AzaleaRegistry;
import com.azalealibrary.azaleacore.foundation.registry.MinigameIdentifier;
import com.azalealibrary.azaleacore.manager.PlaygroundManager;
import com.azalealibrary.azaleacore.playground.Playground;
import com.azalealibrary.azaleacore.util.FileUtil;
import com.azalealibrary.azaleacore.util.TextUtil;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PlaygroundCommand extends CommandNode {

    public PlaygroundCommand() {
        super("@playground",
                new Create(),
                new Delete(),
                new CommandNode("minigame",
                        new Configure(),
                        new Minigame.Start(),
                        new Minigame.Stop(),
                        new Minigame.Restart()
                )
        );
    }

    private static final class Create extends CommandNode {

        public Create() {
            super("create");
        }

        @Override
        public List<String> complete(CommandSender sender, Arguments arguments) {
            return arguments.size() == 1
                    ? AzaleaRegistry.MINIGAME.getObjects().stream().map(MinigameIdentifier::getNamespace).toList()
                    : arguments.size() == 2 ? FileUtil.getMaps().stream().map(File::getName).toList() : arguments.size() == 3
                    ? List.of("<name>") : new ArrayList<>();
        }

        @Override
        public void execute(CommandSender sender, Arguments arguments) {
            MinigameIdentifier identifier = arguments.find(0, "minigame", input -> AzaleaRegistry.MINIGAME.getObjects().stream().filter(key -> Objects.equals(key.getNamespace(), input)).findFirst().orElse(null));
            File map = arguments.find(1, "map", FileUtil::getMap);
            String name = arguments.notMissing(2, "name");

            ChatMessage.info("Creating playground...").post(AzaleaCore.PLUGIN_ID, sender);
            Playground playground = PlaygroundManager.getInstance().create(name, identifier, map);
            ChatMessage.info("Created playground " + TextUtil.getName(playground) + ".").post(AzaleaCore.PLUGIN_ID, sender);
        }
    }

    private static final class Delete extends CommandNode {

        public Delete() {
            super("delete");
        }

        @Override
        public List<String> complete(CommandSender sender, Arguments arguments) {
            return arguments.size() == 1 ? PlaygroundManager.getInstance().getKeys() : new ArrayList<>();
        }

        @Override
        public void execute(CommandSender sender, Arguments arguments) {
            Playground playground = arguments.find(0, "playground", PlaygroundManager.getInstance()::get);

            ChatMessage.info("Deleting playground...").post(AzaleaCore.PLUGIN_ID, sender);
            PlaygroundManager.getInstance().remove(playground);
            ChatMessage.info("Deleted playground " + TextUtil.getName(playground) + ".").post(AzaleaCore.PLUGIN_ID, sender);
        }
    }

    private static abstract class Minigame extends CommandNode {

        protected Minigame(String name) {
            super(name);
        }

        @Override
        public List<String> complete(CommandSender sender, Arguments arguments) {
            return arguments.size() == 1 ? PlaygroundManager.getInstance().getKeys() : new ArrayList<>();
        }

        @Override
        public void execute(CommandSender sender, Arguments arguments) {
            Playground playground = arguments.find(0, "playground", PlaygroundManager.getInstance()::get);
            execute(playground, arguments.subArguments(1));
        }

        protected abstract void execute(Playground playground, Arguments arguments);

        private static final class Start extends Minigame {
            public Start() {
                super("start");
            }

            @Override
            protected void execute(Playground playground, Arguments arguments) {
                playground.start();
            }
        }

        private static final class Stop extends Minigame {
            public Stop() {
                super("stop");
            }

            @Override
            protected void execute(Playground playground, Arguments arguments) {
                playground.stop(ChatMessage.info(String.join(" ", arguments.subArguments(0))));
            }
        }

        private static final class Restart extends Minigame {
            public Restart() {
                super("restart");
            }

            @Override
            protected void execute(Playground playground, Arguments arguments) {
                playground.stop(ChatMessage.info(String.join(" ", arguments.subArguments(0))));
                playground.start();
            }
        }
    }

    private static final class Configure extends ConfigureCommand {
        @Override
        protected List<String> completeConfigurable(CommandSender sender, Arguments arguments) {
            return PlaygroundManager.getInstance().getKeys();
        }

        @Override
        protected @Nullable Configurable getConfigurable(String input) {
            return Optional.ofNullable(PlaygroundManager.getInstance().get(input)).map(Playground::getMinigame).orElse(null);
        }
    }
}
