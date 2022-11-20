package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaPlaygroundApi;
import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.command.core.CommandHandler;
import com.azalealibrary.azaleacore.room.Playground;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.List;

public class PlaygroundCommand {

    private static final String CREATE = "create";
    private static final String DELETE = "delete";

    public PlaygroundCommand(CommandHandler handler) {
        handler.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(CREATE, DELETE));
        handler.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.get(0).equals(DELETE), (sender, arguments) -> AzaleaPlaygroundApi.getInstance().getKeys());
        handler.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.get(0).equals(CREATE), (sender, arguments) -> FileUtil.templates().stream().map(File::getName).toList());
        handler.executeWhen((sender, arguments) -> arguments.get(0).equals(CREATE), this::create);
        handler.executeWhen((sender, arguments) -> arguments.get(0).equals(DELETE), this::delete);
    }

    private Message create(CommandSender sender, Arguments arguments) {
        File template = arguments.parse(1, "Could not find template '%s'.", FileUtil::template);
        String name = arguments.missing(2);

        if (AzaleaPlaygroundApi.getInstance().get(name) != null) {
            return ChatMessage.failure("Playground '" + name + "' already exists.");
        }

        List<String> tags = arguments.subList(3, arguments.size());
        Playground playground = new Playground(name, template, tags);
        AzaleaPlaygroundApi.getInstance().add(name, playground);

        StringBuilder builder = new StringBuilder("Playground '" + name + "' created");
        if (!tags.isEmpty()) builder.append(" with tags: ").append(String.join(" ", tags));
        builder.append(".");

        return ChatMessage.success(builder.toString());
    }

    private Message delete(CommandSender sender, Arguments arguments) {
        Playground playground = arguments.parse(1, "Could not find playground '%s'.", input -> AzaleaPlaygroundApi.getInstance().get(input));
        AzaleaPlaygroundApi.getInstance().remove(playground);

        return ChatMessage.success("Deleted '" + playground.getName() + "' playground.");
    }
}
