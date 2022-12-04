package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.message.Message;
import com.azalealibrary.azaleacore.foundation.registry.AzaleaRegistry;
import com.azalealibrary.azaleacore.foundation.registry.MinigameIdentifier;
import com.azalealibrary.azaleacore.manager.PlaygroundManager;
import com.azalealibrary.azaleacore.playground.Playground;
import com.azalealibrary.azaleacore.util.FileUtil;
import com.azalealibrary.azaleacore.util.TextUtil;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.List;
import java.util.Objects;

@AzaCommand(name = "!playground")
public class PlaygroundCommand extends AzaleaCommand {

    private static final String CREATE = "create";
    private static final String DELETE = "delete";

    public PlaygroundCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(CREATE, DELETE));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, CREATE), (sender, arguments) -> AzaleaRegistry.MINIGAME.getObjects().stream().map(MinigameIdentifier::getNamespace).toList());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 3 && arguments.is(0, CREATE), (sender, arguments) -> FileUtil.getMaps().stream().map(File::getName).toList());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 4 && arguments.is(0, CREATE), (sender, arguments) -> List.of("<name>"));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, DELETE), (sender, arguments) -> PlaygroundManager.getInstance().getKeys());
        configurator.executeWhen((sender, arguments) -> arguments.size() == 4 && arguments.is(0, CREATE), this::create);
        configurator.executeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, DELETE), this::delete);
    }

    private Message create(CommandSender sender, Arguments arguments) {
        MinigameIdentifier identifier = arguments.find(1, "minigame", input -> AzaleaRegistry.MINIGAME.getObjects().stream().filter(key -> Objects.equals(key.getNamespace(), input)).findFirst().orElse(null));
        File map = arguments.find(2, "map", FileUtil::getMap);
        String name = arguments.notMissing(3, "name");

//        ChatMessage.info("Created playground...");
        Playground playground = PlaygroundManager.getInstance().create(name, identifier, map);

        return ChatMessage.info("Created playground " + TextUtil.getName(playground) + ".");
    }

    private Message delete(CommandSender sender, Arguments arguments) {
        Playground playground = arguments.find(1, "playground", PlaygroundManager.getInstance()::get);

//        ChatMessage.info("Deleting playground...");
        PlaygroundManager.getInstance().delete(playground);

        return ChatMessage.info("Deleted playground " + TextUtil.getName(playground) + ".");
    }
}
