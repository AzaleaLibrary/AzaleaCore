package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.minigame.round.RoundTeams;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public final class Hooks {

    public static void showStartScreen(Round<?> round) {
        round.getRoundTeams().getOriginalTeams().forEach((team, players) -> {
            String role = "You are in the " + team.getColor() + team.getName() + ChatColor.RESET + " team.";
            ChatMessage message = new ChatMessage(role);
            ChatMessage objective = new ChatMessage(team.getObjective());

            for (Player player : players) {
                message.post("", player); // TODO - prefix
                objective.post("", player); // TODO - prefix
                player.playSound(player.getLocation(), team.getSound(), 1, 1);
            }
        });
    }

    public static void showEndScreen(Round<?> round, WinCondition<?> win) {
        Team winningTeam = win.getWinningTeam();
        RoundTeams teams = round.getRoundTeams();

        String winners = round.getRoundTeams().getPlayers().stream()
                .filter(player -> teams.isInTeam(player, winningTeam))
                .map(Player::getDisplayName)
                .collect(Collectors.joining(", "));
        ChatMessage won = new ChatMessage("Winners: " + ChatColor.YELLOW + winners);

        String losers = round.getRoundTeams().getPlayers().stream()
                .filter(player -> !teams.isInTeam(player, winningTeam))
                .map(Player::getDisplayName)
                .collect(Collectors.joining(", "));
        ChatMessage lost = new ChatMessage("Losers: " + ChatColor.YELLOW + losers);

        String t = winningTeam.getColor() + winningTeam.getName() + ChatColor.RESET;
        String r = ChatColor.GRAY + win.getReason();
        ChatMessage reason = new ChatMessage(t + " team won! : " + r);

        round.getRoundTeams().getOriginalTeams().forEach((team, players) -> {
            for (Player player : players) {
                reason.post("", player); // TODO - prefix
                won.post("", player); // TODO - prefix
                lost.post("", player); // TODO - prefix

                Sound sound = teams.isInTeam(player, winningTeam)
                        ? Sound.UI_TOAST_CHALLENGE_COMPLETE
                        : Sound.ENTITY_CHICKEN_AMBIENT;
                player.playSound(player.getLocation(), sound, 1, 1);
            }
        });
    }
}
