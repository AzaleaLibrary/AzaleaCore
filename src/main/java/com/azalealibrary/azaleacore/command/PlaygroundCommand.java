package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.registry.AzaleaRegistry;
import com.azalealibrary.azaleacore.foundation.registry.MinigameIdentifier;
import com.azalealibrary.azaleacore.manager.PartyManager;
import com.azalealibrary.azaleacore.manager.PlaygroundManager;
import com.azalealibrary.azaleacore.party.Party;
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
                new Reserve(), // TODO - remove as standalone command?
                new CommandNode("minigame",
                        new Minigame.Configure(),
                        new Minigame.Command()
                ),
                new CommandNode("round",
                        new Round.Start(),
                        new Round.Stop(),
                        new Round.Restart()
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
            PlaygroundManager.getInstance().delete(playground);
            ChatMessage.info("Deleted playground " + TextUtil.getName(playground) + ".").post(AzaleaCore.PLUGIN_ID, sender);
        }
    }

    private static final class Reserve extends CommandNode {

        public Reserve() {
            super("reserve");
        }

        @Override
        public List<String> complete(CommandSender sender, Arguments arguments) {
            return arguments.size() == 1
                    ? PlaygroundManager.getInstance().getKeys() : arguments.size() == 2
                    ? PartyManager.getInstance().getKeys() : new ArrayList<>();
        }

        @Override
        public void execute(CommandSender sender, Arguments arguments) {
            Playground playground = arguments.find(0, "playground", PlaygroundManager.getInstance()::get);
            Party party = arguments.find(1, "party", PartyManager.getInstance()::get);
            playground.setParty(party);
        }
    }

    private static abstract class Round extends CommandNode {

        public Round(String name) {
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

        private static final class Start extends Round {

            public Start() {
                super("start");
            }

            @Override
            protected void execute(Playground playground, Arguments arguments) {
                playground.start();
            }
        }

        private static final class Stop extends Round {

            public Stop() {
                super("stop");
            }

            @Override
            protected void execute(Playground playground, Arguments arguments) {
                playground.stop(ChatMessage.info(String.join(" ", arguments.subArguments(0))));
            }
        }

        private static final class Restart extends Round {

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

    private static final class Minigame {

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

        private static final class Command extends CommandNode {

            public Command() {
                super("command");
            }

            @Override
            public List<CommandNode> getChildren(CommandSender sender, Arguments arguments) {
//                if (arguments.size() == 2) {
//                    Playground playground = arguments.find(0, "playground", PlaygroundManager.getInstance()::get);
//                    children = playground.getMinigame().getCommands();
//                    System.out.println(playground.getMinigame().getCommands().size());
//                    System.out.println(children);
//                    System.out.println(arguments);
//                    return super.getChildren(sender, arguments.subArguments(0));
//                }
//                System.out.println(children);
////                return getClosestMatch(sender, playground.getMinigame().getCommands(), arguments, 0, null).getKey().getChildren(sender, arguments);
//                return super.getChildren(sender, arguments);
//                if (children.isEmpty() && arguments.size() > 0) {
//                    Playground playground = PlaygroundManager.getInstance().get(arguments.getLast());
//                    children = playground != null ? playground.getMinigame().getCommands() : new ArrayList<>();
////                    System.out.println(children);
//                    System.out.println(arguments);
//                }
//                return List.of(new CommandNode(arguments.getLast(), playground != null ? playground.getMinigame().getCommands().toArray(CommandNode[]::new): new CommandNode[0]));
//                Playground playground = PlaygroundManager.getInstance().get(arguments.getLast());
//                System.out.println(playground);
//                System.out.println(arguments);
//                if (playground == null) {
//                    return super.getChildren(sender, arguments);
//                }
//
////                System.out.println(getClosestMatch(sender, playground.getMinigame().getCommands(), arguments, 0, null));
//                Map.Entry<CommandNode, Arguments> pair = getClosestMatch(sender, playground.getMinigame().getCommands(), arguments.subArguments(1), 0, this);
//                System.out.println(pair);
//
//                if (pair.getKey() != null && arguments.size() > 2) {
//                    return pair.getKey().getChildren(sender, pair.getValue());
//                }

                Playground playground = PlaygroundManager.getInstance().get(arguments.get(0));
                System.out.println(playground);
                System.out.println(arguments);

                if (playground != null && arguments.size() <= 2) {
                    return playground.getMinigame().getCommands();
                }
                return super.getChildren(sender, arguments.subArguments(1));
            }

            @Override
            public List<String> complete(CommandSender sender, Arguments arguments) {
//                System.out.println("complete " + arguments);
//                return arguments.size() == 1 ? PlaygroundManager.getInstance().getKeys() : super.complete(sender, arguments.subArguments(0));
//                if (arguments.size() == 1) {
//                    return PlaygroundManager.getInstance().getKeys();
//                }
//                return super.complete(sender, arguments.subArguments(1));
//                Playground playground = arguments.find(0, "playground", PlaygroundManager.getInstance()::get);
//                return playground.getMinigame().getCommands();
//                return super.complete(sender, arguments);
//                Playground playground = PlaygroundManager.getInstance().get(arguments.get(1));
//                Arguments sub = playground != null ? arguments.subArguments(1) : arguments;
                return arguments.size() == 1 ? PlaygroundManager.getInstance().getKeys() : super.complete(sender, arguments);
            }
        }
    }
}
