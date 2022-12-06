package com.azalealibrary.azaleacore.manager;

import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PartyManager extends Manager<Party> {

    private static final PartyManager MANAGER = new PartyManager();

    public static PartyManager getInstance() {
        return MANAGER;
    }

    private PartyManager() {
        super("party");
    }

    public @Nullable Party get(Player player) {
        return getAll().stream().filter(p -> p.isMember(player)).findFirst().orElse(null);
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
        section.set("players", party.getPlayers().stream().map(p -> p.getUniqueId().toString()).toList());
        party.getConfiguration().serialize(section.createSection("configs"));
    }

    @Override
    protected Party deserializeEntry(ConfigurationSection section) {
        Party party = new Party(section.getString("name"));
        party.getConfiguration().deserialize(Objects.requireNonNull(section.getConfigurationSection("configs")));
        party.getPlayers().add(party.getConfiguration().getPartyOwner());
        List<String> ids = Objects.requireNonNull((List<String>) section.getList("players"));

        for (String id : ids) {
            Player player = Bukkit.getPlayer(UUID.fromString(id));

            if (player != null) {
                party.getPlayers().add(player);
            }
        }
        return party;
    }
}
