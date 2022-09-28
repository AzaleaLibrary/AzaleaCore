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
import java.util.Comparator;
import java.util.List;

// TODO - currently composition-based, what about inheritance?
public abstract class AzaleaCommand implements CommandExecutor, TabCompleter {

    public static final String COMMAND_PREFIX = "!";
    private static final String COMMAND_TEXT_PREFIX = "AZA";

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String ignored, @Nonnull String[] args) {
        execute(sender, Arrays.asList(args)).post(COMMAND_TEXT_PREFIX, sender);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        List<String> params = Arrays.asList(args);
        List<String> output = new ArrayList<>(onTabComplete(sender, params));
        String last = params.size() > 0 ? params.get(params.size() - 1) : "";
        output.sort(Comparator.<String, Boolean>comparing(s -> s.contains(last)).reversed());
        return output;
    }

    protected abstract Message execute(@Nonnull CommandSender sender, List<String> params);

    protected abstract List<String> onTabComplete(CommandSender sender, List<String> params);

    private static List<String> getMatching(String param, List<String> params) {
        if (param.equals("")) return params;
        System.out.println("getMatching" + params);
        ArrayList<String> matching = new ArrayList<>();

        for (String text : params) {
            if (StringUtil.startsWithIgnoreCase(text, param)) {
                matching.add(text);
            }
        }

        matching.sort(String.CASE_INSENSITIVE_ORDER);
        return matching;
    }
}
