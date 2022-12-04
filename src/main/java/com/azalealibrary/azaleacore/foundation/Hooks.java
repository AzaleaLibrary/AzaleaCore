package com.azalealibrary.azaleacore.foundation;

import com.azalealibrary.azaleacore.api.MinigameTeam;
import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.message.TitleMessage;
import com.azalealibrary.azaleacore.party.Party;
import com.azalealibrary.azaleacore.round.RoundTeams;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Hooks { // TODO - redundant?

    public static void showStartScreen(RoundTeams teams, Party party) {
        teams.getOriginalTeams().forEach((team, players) -> {
            String role = "You are in the " + team.getColor() + team.getName() + ChatColor.RESET + " team.";
            ChatMessage message =  ChatMessage.info(role);
            ChatMessage objective =  ChatMessage.info(team.getObjective());

            for (Player player : players) {
                party.broadcast(player, message);
                party.broadcast(player, objective);
                player.playSound(player.getLocation(), team.getSound(), 1, 1);
            }
        });
    }

    public static void showWinScreen(RoundTeams teams, Party party, WinCondition winCondition) {
        MinigameTeam winningMinigameTeam = winCondition.getWinningTeam();

        String teamWon = winningMinigameTeam.getColor() + winningMinigameTeam.getName() + ChatColor.RESET + " Won!";
        String reason = ChatColor.GRAY + winCondition.getReason();

        TitleMessage title = new TitleMessage(teamWon, reason);
        ChatMessage message = ChatMessage.info(teamWon + " : " + reason);

        party.broadcast(title);
        party.broadcast(message);

        for (Map.Entry<MinigameTeam, List<Player>> entry : teams.getTeams().entrySet()) {
            MinigameTeam minigameTeam = entry.getKey();
            List<Player> players = entry.getValue();

            String formatted = players.stream().map(Player::getDisplayName).collect(Collectors.joining(", "));
            String list = (formatted.isEmpty() ? "No players..." : formatted);
            String color = formatted.isEmpty() ? ChatColor.GRAY.toString() + ChatColor.ITALIC
                    : ChatColor.YELLOW.toString();
            String line = minigameTeam.getColor() + minigameTeam.getName() + ChatColor.RESET + " : " + color + list;
            party.broadcast(ChatMessage.info(line));

            for (Player player : players) {
                Sound sound = minigameTeam == winningMinigameTeam
                        ? Sound.UI_TOAST_CHALLENGE_COMPLETE
                        : Sound.ENTITY_CHICKEN_AMBIENT;
                player.playSound(player.getLocation(), sound, 1, 1);
            }
        }
    }

    public static void awardPoints(RoundTeams teams, WinCondition winCondition) {
//        for (Map.Entry<MinigameTeam, List<Player>> entry : teams.getTeams().entrySet()) {
//            if (entry.getKey() == winCondition.getWinningTeam()) {
//                for (Player player : entry.getValue()) {
//                    AzaleaScoreboardApi.getInstance().award(player, winCondition);
//                }
//            }
//        }
    }
}
