package com.azalealibrary.azaleacore.party;

import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.message.Message;
import com.azalealibrary.azaleacore.util.TextUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Party {

    private final String name;
    private final List<Player> players = new ArrayList<>();
    private final List<Player> invitations = new ArrayList<>();
    private final PartyConfiguration configuration;

    public Party(String name) {
        this.name = name;
        this.configuration = new PartyConfiguration();
    }

    public String getName() {
        return name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public PartyConfiguration getConfiguration() {
        return configuration;
    }

    public void invitePlayer(Player player) {
        if (isInvited(player)) {
            throw new AzaleaException("Player " + TextUtil.getName(player) + " already invited.");
        }
        invitations.add(player);
    }

    public void withdrawInvitation(Player player) {
        if (!isInvited(player)) {
            throw new AzaleaException("Player " + TextUtil.getName(player) + " not invited.");
        }
        invitations.remove(player);
    }

    public boolean isInvited(Player player) {
        return invitations.contains(player);
    }

    public boolean isHere(Player player) {
        return players.contains(player);
    }

    public void addPlayer(Player player, boolean notifyMembers) {
        if (isHere(player)) {
            throw new AzaleaException("Player already in party.");
        } else if (configuration.joinWithInvitation() && !isInvited(player)) {
            throw new AzaleaException("Sorry, you have not been invited :(");
        }

        players.add(player);
        invitations.remove(player); // TODO - remove player from whitelist?

        if (notifyMembers) {
            broadcast(ChatMessage.announcement(TextUtil.getName(player) + " has joined the party."));
        }
    }

    public void removePlayer(Player player, boolean notifyMembers) {
        if (!isHere(player)) {
            throw new AzaleaException("Player not in party.");
        }

        players.remove(player);

        if (notifyMembers) {
            broadcast(ChatMessage.announcement(TextUtil.getName(player) + " has left the party."));
        }
    }

    public void broadcast(Message message) {
        players.forEach(player -> broadcast(player, message));
    }

    public void broadcast(Player player, Message message) {
        message.post(name, player);
    }
}
