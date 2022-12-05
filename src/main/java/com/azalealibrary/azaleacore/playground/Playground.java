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
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
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
        if (hasParty()) {
            throw new AzaleaException("Another party is on this playground.");
        }

        this.party = party;
        for (Player player : party.getPlayers()) {
            player.teleport(world.getSpawnLocation());
        }
    }

    public boolean hasParty() {
        return party != null;
    }

    public boolean hasOngoingRound() {
        return ticker.isRunning();
    }

    public void start() {
        verifyCanStart();

        List<Player> players = party.getPlayers();
        players.forEach(player -> player.teleport(world.getSpawnLocation().clone().add(.5, 0, .5)));
        RoundTeams teams = RoundTeams.generate(minigame.getPossibleTeams(), players);
        Round round = new Round(party, world, minigame, teams);
        ticker.start(round);
    }

    public void stop(Message reason) {
        verifyCanStop();

        party.broadcast(reason);
        ticker.stop();
    }

    public void clear() {
        if (hasOngoingRound()) {
            stop(ChatMessage.important("Playground has been purged."));
        }
        if (hasParty()) {
            for (Player player : party.getPlayers()) {
                player.teleport(AzaleaConfiguration.getInstance().getServerLobby().getSpawnLocation());
            }
            this.party = null;
        }
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
