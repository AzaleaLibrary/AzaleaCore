package com.azalealibrary.azaleacore.party;

import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.message.Message;
import com.azalealibrary.azaleacore.util.TextUtil;
import com.google.common.collect.ImmutableSet;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Party {

    private final String name;
    private final Set<Player> players = new HashSet<>();
    private final Set<Player> invitations = new HashSet<>();
    private final PartyConfiguration configuration;

    public Party(String name) {
        this.name = name;
        this.configuration = new PartyConfiguration();
    }

    public String getName() {
        return name;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public PartyConfiguration getConfiguration() {
        return configuration;
    }

    public ImmutableSet<Player> getInvitations() {
        return ImmutableSet.copyOf(invitations);
    }

    public void invitePlayer(Player player) {
        if (isInvited(player)) {
            throw new AzaleaException(TextUtil.getName(player) + " has already been invited.");
        }

        broadcast(ChatMessage.announcement(TextUtil.getName(player) + " has been invited."));
        invitations.add(player);
    }

    public void withdrawInvitation(Player player) {
        if (!isInvited(player)) {
            throw new AzaleaException(TextUtil.getName(player) + " has not been invited yet.");
        }

        broadcast(ChatMessage.announcement("Invitation withdrawn for " + TextUtil.getName(player) + "."));
        invitations.remove(player);
    }

    public boolean isInvited(Player player) {
        return invitations.contains(player);
    }

    public boolean isHere(Player player) {
        return players.contains(player);
    }

    public void addPlayer(Player player) {
        if (isHere(player)) {
            throw new AzaleaException("Player already in party.");
        } else if (configuration.joinWithInvitation() && !isInvited(player)) {
            throw new AzaleaException("Sorry, you have not been invited :(");
        }

        players.add(player);
        invitations.remove(player); // TODO - remove player from whitelist?
        broadcast(ChatMessage.announcement(TextUtil.getName(player) + " has joined the party."));
    }

    public void removePlayer(Player player) {
        if (!isHere(player)) {
            throw new AzaleaException("Player not in party.");
        }

        players.remove(player);
        broadcast(ChatMessage.announcement(TextUtil.getName(player) + " has left the party."));
    }

    public void broadcast(Message message) {
        players.forEach(player -> broadcast(player, message));
    }

    public void broadcast(Player player, Message message) {
        message.post(name, player);
    }
}
