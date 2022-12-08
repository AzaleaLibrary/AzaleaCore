package com.azalealibrary.azaleacore.party;

import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.message.Message;
import com.azalealibrary.azaleacore.manager.PartyManager;
import com.azalealibrary.azaleacore.util.TextUtil;
import com.google.common.collect.ImmutableSet;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Random;
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
        if (isMember(player)) {
            throw new AzaleaException(TextUtil.getName(player) + " is already a member of the party.");
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

    public boolean isMember(Player player) {
        return players.contains(player);
    }

    public void addPlayer(Player player) {
        if (isMember(player)) {
            throw new AzaleaException("Player already in party.");
        } else if (configuration.joinWithInvitation() && !isInvited(player)) {
            throw new AzaleaException("Sorry, you have not been invited :(");
        }

        players.add(player);
        invitations.remove(player); // TODO - remove player from whitelist?
        broadcast(ChatMessage.announcement(TextUtil.getName(player) + " has joined the party."));
    }

    public void removePlayer(Player player) {
        if (!isMember(player)) {
            throw new AzaleaException("Player not in party.");
        }

        players.remove(player);
        invitations.remove(player);
        broadcast(ChatMessage.announcement(TextUtil.getName(player) + " has left the party."));

        if (players.size() == 0) {
            // if no player is present, delete the party
            PartyManager.getInstance().remove(this);
        } else if (player == configuration.getPartyOwner()) {
            // set new owner if old one is removed
            Player owner = (Player) players.toArray()[new Random().nextInt(players.size())];
            configuration.setPartyOwner(owner);
            broadcast(ChatMessage.important(TextUtil.getName(owner) + " has been appointed as new party owner."));
        }
    }

    public void broadcast(Message message) {
        players.forEach(player -> broadcast(player, message));
    }

    public void broadcast(Player player, Message message) {
        message.post(name, player);
    }
}
