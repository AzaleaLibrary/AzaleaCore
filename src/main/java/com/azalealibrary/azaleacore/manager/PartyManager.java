package com.azalealibrary.azaleacore.manager;

import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.party.Party;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class PartyManager extends Manager<Party> {

    private static final PartyManager MANAGER = new PartyManager();

    public static PartyManager getInstance() {
        return MANAGER;
    }

    private PartyManager() {
        super("party");
    }

    public @Nullable Party get(Player player) {
        return getAll().stream().filter(p -> p.isHere(player)).findFirst().orElse(null);
    }

    public Party create(String name, Player owner) {
        Party party = new Party(name);
        party.getConfiguration().setPartyOwner(owner);
        party.getPlayers().add(owner);
        add(name, party);
        return party;
    }

    public void delete(Party party) {
        party.broadcast(ChatMessage.important("Party has been disbanded."));
        party.getConfiguration().setPartyOwner(null);
        party.getPlayers().clear();
        remove(party);
    }

    @Override
    protected void serializeEntry(ConfigurationSection section, Party party) {
        section.set("name", party.getName());
        section.set("players", party.getPlayers().stream().toList());
        party.getConfiguration().serialize(section.createSection("configs"));
    }

    @Override
    protected Party deserializeEntry(ConfigurationSection section) {
        Party party = new Party(section.getString("name"));
        party.getPlayers().addAll(Objects.requireNonNull((List<Player>) section.getList("players")));
        party.getConfiguration().deserialize(Objects.requireNonNull(section.getConfigurationSection("configs")));
        return party;
    }
}
