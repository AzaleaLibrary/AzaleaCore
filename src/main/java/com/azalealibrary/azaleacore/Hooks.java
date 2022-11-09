package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.Round;
import com.azalealibrary.azaleacore.api.Team;
import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.broadcast.message.TitleMessage;
import com.azalealibrary.azaleacore.minigame.round.RoundTeams;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Hooks {

    public static void showStartScreen(Round<?> round) {
        Broadcaster broadcaster = round.getBroadcaster();

        round.getRoundTeams().getOriginalTeams().forEach((team, players) -> {
            String role = "You are in the " + team.getColor() + team.getName() + ChatColor.RESET + " team.";
            ChatMessage message = new ChatMessage(role);
            ChatMessage objective = new ChatMessage(team.getObjective());

            for (Player player : players) {
                broadcaster.toPlayer(player, message);
                broadcaster.toPlayer(player, objective);
                player.playSound(player.getLocation(), team.getSound(), 1, 1);
            }
        });
    }

    public static void showEndScreen(Round<?> round, WinCondition<?> winCondition) {
        Broadcaster broadcaster = round.getBroadcaster();
        Team winningTeam = winCondition.getWinningTeam();
        RoundTeams teams = round.getRoundTeams();

        String teamWon = winningTeam.getColor() + winningTeam.getName() + ChatColor.RESET + " Wins!";
        String reason = ChatColor.GRAY + winCondition.getReason();

        TitleMessage title = new TitleMessage(teamWon, reason);
        ChatMessage message = new ChatMessage(teamWon + " : " + reason);

        broadcaster.broadcast(title);
        broadcaster.broadcast(message);

        for (Map.Entry<Team, List<Player>> entry : teams.getTeams().entrySet()) {
            Team team = entry.getKey();
            List<Player> players = entry.getValue();

            String formatted = players.stream().map(Player::getDisplayName).collect(Collectors.joining(", "));
            String list = (formatted.isEmpty() ? "No players..." : formatted);
            String color = formatted.isEmpty() ? ChatColor.GRAY.toString() + ChatColor.ITALIC : ChatColor.YELLOW.toString();
            String line = team.getColor() + team.getName() + ChatColor.RESET + " : " + color + list;
            broadcaster.broadcast(new ChatMessage(line));

            for (Player player : players) {
                Sound sound = team == winningTeam ? Sound.UI_TOAST_CHALLENGE_COMPLETE : Sound.ENTITY_CHICKEN_AMBIENT;
                player.playSound(player.getLocation(), sound, 1, 1);
            }
        }
    }
}
