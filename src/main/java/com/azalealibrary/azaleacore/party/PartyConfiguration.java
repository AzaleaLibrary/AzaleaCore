package com.azalealibrary.azaleacore.party;

import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.configuration.property.AssignmentPolicy;
import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import com.azalealibrary.azaleacore.foundation.configuration.property.Property;
import com.azalealibrary.azaleacore.foundation.configuration.property.PropertyType;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;

public class PartyConfiguration implements Configurable {

    private final Property<Player> partyOwner = new Property<>(PropertyType.PLAYER, "partyOwner", null, true);
    private final Property<String> joinPassword = new Property<>(PropertyType.STRING, "joinPassword", null, false);
    private final Property<Boolean> joinWithInvitation = new Property<>(PropertyType.BOOLEAN, "joinWithInvitation", false, false);
    private final Property<Boolean> allowSpectators = new Property<>(PropertyType.BOOLEAN, "allowSpectators", true, true);
    private final Property<Integer> playerTimeout = new Property<>(PropertyType.INTEGER, "playerTimeout", 30, true, AssignmentPolicy.POSITIVE_INTEGER);

    public @Nullable String getJoinPassword() {
        return joinPassword.get();
    }

    public void setPartyOwner(Player player) {
        partyOwner.set(player);
    }

    public boolean joinWithInvitation() {
        return joinWithInvitation.get();
    }

    public boolean allowSpectators() {
        return allowSpectators.get();
    }

    public int getPlayerTimeout() {
        return playerTimeout.get();
    }

    @Override
    public List<ConfigurableProperty<?>> getProperties() {
        return List.of(partyOwner, joinPassword, joinWithInvitation, allowSpectators, playerTimeout);
    }
}
