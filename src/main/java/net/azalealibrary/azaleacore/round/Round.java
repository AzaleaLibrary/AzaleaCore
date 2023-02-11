package net.azalealibrary.azaleacore.round;

import net.azalealibrary.azaleacore.minigame.Minigame;
import net.azalealibrary.azaleacore.party.Party;
import org.bukkit.World;

public class Round {

    private final Party party;
    private final World world;
    private final Minigame minigame;
    private final RoundTeams teams;

    public Round(Party party, World world, Minigame minigame, RoundTeams teams) {
        this.party = party;
        this.world = world;
        this.minigame = minigame;
        this.teams = teams;
    }

    public Party getParty() {
        return party;
    }

    public World getWorld() {
        return world;
    }

    public Minigame getMinigame() {
        return minigame;
    }

    public RoundTeams getTeams() {
        return teams;
    }
}
