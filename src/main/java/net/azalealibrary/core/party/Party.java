package net.azalealibrary.core.party;

import com.google.common.collect.ImmutableSet;
import net.azalealibrary.core.foundation.AzaleaException;
import net.azalealibrary.core.foundation.message.ChatMessage;
import net.azalealibrary.core.foundation.message.Message;
import net.azalealibrary.core.manager.PartyManager;
import net.azalealibrary.core.util.TextUtil;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Party {

    private final String name;
    private final Set<Player> players = new HashSet<>();
    private final Set<Player> invitations = new HashSet<>();
    private final Set<Player> requests = new HashSet<>();
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
        } else if (isMember(player)) {
            throw new AzaleaException(TextUtil.getName(player) + " is already a member of the party.");
        }

        broadcast(ChatMessage.announcement(TextUtil.getName(player) + " has been invited."));
        invitations.add(player);
    }

    public void withdrawInvitation(Player player) {
        if (!isInvited(player)) {
            throw new AzaleaException(TextUtil.getName(player) + " has not been invited.");
        }

        broadcast(ChatMessage.announcement("Invitation withdrawn for " + TextUtil.getName(player) + "."));
        invitations.remove(player);
    }

    public boolean isInvited(Player player) {
        return invitations.contains(player);
    }

    public Set<Player> getRequests() {
        return requests;
    }

    public void requestToJoin(Player player) {
        if (hasRequestedToJoin(player)) {
            throw new AzaleaException("Already requested to join.");
        } else if (isMember(player)) {
            throw new AzaleaException(TextUtil.getName(player) + " is already a member of the party.");
        }

        broadcast(ChatMessage.announcement(TextUtil.getName(player) + " requested to join party."));
        requests.add(player);
    }

    public void denyJoinRequest(Player player) {
        if (!hasRequestedToJoin(player)) {
            throw new AzaleaException(TextUtil.getName(player) + " has not requested to join party.");
        }

        broadcast(ChatMessage.announcement(TextUtil.getName(player) + "'s join request has been denied."));
        requests.remove(player);
    }

    public void acceptJoinRequest(Player player) {
        if (!hasRequestedToJoin(player)) {
            throw new AzaleaException(TextUtil.getName(player) + " has not requested to join party.");
        }

        broadcast(ChatMessage.announcement(TextUtil.getName(player) + "'s join request has been accepted."));
        addPlayer(player);
    }

    public boolean hasRequestedToJoin(Player player) {
        return requests.contains(player);
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
        invitations.remove(player);
        requests.remove(player);
        broadcast(ChatMessage.announcement(TextUtil.getName(player) + " has joined the party."));
    }

    public void removePlayer(Player player) {
        if (!isMember(player)) {
            throw new AzaleaException("Player not in party.");
        }

        players.remove(player);
        invitations.remove(player);
        requests.remove(player);
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
