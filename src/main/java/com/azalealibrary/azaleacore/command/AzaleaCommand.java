package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO - currently composition-based, what about inheritance?
public abstract class AzaleaCommand implements CommandExecutor, TabCompleter {

    public static final String COMMAND_PREFIX = "!";
    private static final String COMMAND_TEXT_PREFIX = "AZA";

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String ignored, @Nonnull String[] args) {
        execute(sender, Arrays.copyOfRange(args, 1, args.length)).post(COMMAND_TEXT_PREFIX, sender);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        List<String> params = new ArrayList<>(Arrays.stream(args).filter(text -> !text.equals("")).toList());
        if (params.size() == 0) params = new ArrayList<>(List.of(""));
        return getMatching(params.remove(0), onTabComplete(sender, label, params.toArray(new String[0])));
    }

    protected abstract Message execute(@Nonnull CommandSender sender, String[] params);

    protected abstract List<String> onTabComplete(CommandSender sender, String option, String[] params);

    private static List<String> getMatching(String param, List<String> list) {
        ArrayList<String> matching = new ArrayList<>();

        for (String text : list) {
            if (StringUtil.startsWithIgnoreCase(text, param)) {
                matching.add(text);
            }
        }

        matching.sort(String.CASE_INSENSITIVE_ORDER);
        return matching;
    }
}
