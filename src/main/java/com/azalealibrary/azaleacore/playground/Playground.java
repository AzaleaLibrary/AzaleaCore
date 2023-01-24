package com.azalealibrary.azaleacore.playground;

import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.message.Message;
import com.azalealibrary.azaleacore.minigame.Minigame;
import com.azalealibrary.azaleacore.party.Party;
import com.azalealibrary.azaleacore.round.Round;
import com.azalealibrary.azaleacore.round.RoundTeams;
import com.azalealibrary.azaleacore.round.RoundTicker;
import com.azalealibrary.azaleacore.util.TextUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class Playground {

    private final String name;
    private final World world;
    private final Minigame minigame;
    private final RoundTicker ticker;

    private @Nullable Party party;

    public Playground(String name, World world, Minigame minigame) {
        this.name = name;
        this.world = world;
        this.minigame = minigame;
        this.ticker = new RoundTicker(this);
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public Minigame getMinigame() {
        return minigame;
    }

    public @Nullable Party getParty() {
        return party;
    }

    public void setParty(@Nullable Party party) {
        if (party == null) {
            if (hasOngoingRound()) {
                stop(ChatMessage.important("Playground has been purged."));
            }
            if (hasParty()) {
                for (Player player : party.getPlayers()) {
                    player.teleport(AzaleaConfiguration.getInstance().getServerLobby().getSpawnLocation().clone().add(.5, 0, .5));
                }
            }
        } else if (hasParty()) {
            throw new AzaleaException("Another party is already in this playground.");
        } else if (party == getParty()) {
            throw new AzaleaException("Already on this playground.");
        } else if (party != null) {
            party.getPlayers().forEach(p -> p.teleport(world.getSpawnLocation().clone().add(.5, 0, .5)));
            party.broadcast(ChatMessage.announcement("Party moved to " + TextUtil.getName(this) + "."));
        }
        this.party = party;
    }

    public boolean hasParty() {
        return party != null;
    }

    public boolean hasOngoingRound() {
        return ticker.isRunning();
    }

    public void start() {
        verifyCanStart();

        List<Player> players = party.getPlayers().stream().toList();
        players.forEach(player -> player.teleport(world.getSpawnLocation().clone().add(.5, 0, .5)));
        RoundTeams teams = RoundTeams.create(new ArrayList<>(minigame.getPossibleTeams()), players);
        Round round = new Round(party, world, minigame, teams);
        ticker.start(round);
    }

    public void stop(Message reason) {
        verifyCanStop();

        List<Player> players = party.getPlayers().stream().toList();
        players.forEach(player -> player.teleport(world.getSpawnLocation().clone().add(.5, 0, .5)));
        party.broadcast(reason);
        ticker.stop();
    }

    public void addPlayer(Player player) {
        if (world.getPlayers().contains(player)) {
            throw new AzaleaException(TextUtil.getName(player) + " is already on this playground.");
        }

        if (hasParty()) {
            if (!party.isMember(player) && !party.isInvited(player)) {
                if (!party.getConfiguration().allowSpectators()) {
                    throw new AzaleaException("Sorry, the current party is private.");
                }

                player.setGameMode(GameMode.SPECTATOR);
                party.broadcast(ChatMessage.announcement(TextUtil.getName(player) + " is spectating."));
            } else {
                party.broadcast(ChatMessage.announcement(TextUtil.getName(player) + " has joined the playground."));
            }
        }
        player.teleport(world.getSpawnLocation().clone().add(.5, 0, .5));
    }

    public void removePlayer(Player player) {
        if (!world.getPlayers().contains(player)) {
            throw new AzaleaException(TextUtil.getName(player) + " is not on this playground.");
        }

        if (hasParty()) {
            if (party.isMember(player)) {
                party.broadcast(ChatMessage.announcement(TextUtil.getName(player) + " has left the playground."));

                if (ticker.isRunning()) {
                    ticker.getRound().getTeams().removePlayer(player);
                    party.broadcast(ChatMessage.announcement(TextUtil.getName(player) + " has been removed from the round."));
                }
            } else {
                player.setGameMode(GameMode.ADVENTURE); // TODO - default gamemode?
                party.broadcast(ChatMessage.announcement(TextUtil.getName(player) + " is no longer spectating."));
            }
        }
        player.teleport(AzaleaConfiguration.getInstance().getServerLobby().getSpawnLocation().clone().add(.5, 0, .5));
    }

    private void verifyCanStart() {
        if (!hasParty()) {
            throw new AzaleaException("No party in playground.");
        } else if (ticker.isRunning()) {
            throw new AzaleaException("Round already ongoing.");
        }

        String[] properties = minigame.getProperties().stream()
                .filter(property -> property.isRequired() & !property.isSet())
                .map(property -> "> " + ChatColor.ITALIC + property.getName())
                .toArray(String[]::new);

        if (properties.length > 0) {
            throw new AzaleaException("Some required minigame properties are missing:", properties);
        }
    }

    private void verifyCanStop() {
        if (!hasParty()) {
            throw new AzaleaException("No party in playground.");
        } else if (!ticker.isRunning()) {
            throw new AzaleaException("No round currently running.");
        }
    }
}
