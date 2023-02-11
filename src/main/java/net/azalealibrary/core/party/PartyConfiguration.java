package net.azalealibrary.core.party;

import net.azalealibrary.core.foundation.configuration.Configurable;
import net.azalealibrary.core.foundation.configuration.property.AssignmentPolicy;
import net.azalealibrary.core.foundation.configuration.property.ConfigurableProperty;
import net.azalealibrary.core.foundation.configuration.property.Property;
import net.azalealibrary.core.foundation.configuration.property.PropertyType;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;

public class PartyConfiguration implements Configurable {

    private final Property<Player> partyOwner = new Property<>(PropertyType.PLAYER, "partyOwner", "The party owner has complete control over the party.", null, true);
    private final Property<String> joinPassword = new Property<>(PropertyType.STRING, "joinPassword", "Specify a password when joining your party.", null, false);
    private final Property<Boolean> joinWithInvitation = new Property<>(PropertyType.BOOLEAN, "joinWithInvitation", "Whether players must be explicitly invited to join the party.", false, false);
    private final Property<Boolean> allowSpectators = new Property<>(PropertyType.BOOLEAN, "allowSpectators", "When on a playground, whether to allow spectators.", true, true);
    private final Property<Integer> playerTimeout = new Property<>(PropertyType.INTEGER, "playerTimeout", "When a player quits, specify how much time in seconds they are removed from the party.", 30, true, AssignmentPolicy.POSITIVE_INTEGER);

    public @Nullable String getJoinPassword() {
        return joinPassword.get();
    }

    public Player getPartyOwner() {
        return partyOwner.get();
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
