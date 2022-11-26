package com.azalealibrary.azaleacore.room;

import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.configuration.property.AssignmentPolicy;
import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import com.azalealibrary.azaleacore.foundation.configuration.property.Property;
import com.azalealibrary.azaleacore.foundation.configuration.property.PropertyType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class RoomConfiguration implements Configurable {

    // required
    private final Property<Integer> roundGracePeriod = new Property<>(PropertyType.INTEGER, "roundGracePeriod", 3, true, AssignmentPolicy.POSITIVE_INTEGER);
    private final Property<Integer> roundDurationPeriod = new Property<>(PropertyType.INTEGER, "roundDurationPeriod", 30, true, AssignmentPolicy.POSITIVE_INTEGER);
    private final Property<Integer> roundTickRate = new Property<>(PropertyType.INTEGER, "roundTickRate", 1, true, AssignmentPolicy.POSITIVE_INTEGER, AssignmentPolicy.create(value -> value <= 20, "Round tick rate can not be more than 20."));
    private final Property<Integer> maximumPlayer = new Property<>(PropertyType.INTEGER, "maximumPlayer", 4, true, AssignmentPolicy.POSITIVE_INTEGER, AssignmentPolicy.create(value -> value <= Bukkit.getServer().getMaxPlayers(), "Can not exceed max server player count."));
    private final Property<Integer> minimumPlayer = new Property<>(PropertyType.INTEGER, "minimumPlayer", 2, true, AssignmentPolicy.POSITIVE_INTEGER, AssignmentPolicy.create(value -> maximumPlayer.get() < value, "Can not be more than max players."));

    // optional
    private final Property<String> joinPassword = new Property<>(PropertyType.STRING, "joinPassword", null, false);
    private final Property<Boolean> joinWithInvitation = new Property<>(PropertyType.BOOLEAN, "joinWithInvitation", false, false);
    private final Property<Boolean> allowSpectators = new Property<>(PropertyType.BOOLEAN, "allowSpectators", true, false);
    private final Property<Player> roomOwner;

    public RoomConfiguration(Player owner) {
        roomOwner = new Property<>(PropertyType.PLAYER, "roomOwner", owner, false);
    }

    public int getRoundGracePeriod() {
        return roundGracePeriod.get();
    }

    public int getRoundDurationPeriod() {
        return roundDurationPeriod.get();
    }

    public int getRoundTickRate() {
        return roundTickRate.get();
    }

    public int getMaximumPlayer() {
        return maximumPlayer.get();
    }

    public int getMinimumPlayer() {
        return minimumPlayer.get();
    }

    public String getJoinPassword() {
        return joinPassword.get();
    }

    public boolean joinWithInvitation() {
        return joinWithInvitation.get();
    }

    public boolean allowSpectators() {
        return allowSpectators.get();
    }

    public Player getRoomOwner() {
        return roomOwner.get();
    }

    @Override
    public List<ConfigurableProperty<?>> getProperties() {
        return List.of(roundGracePeriod, roundDurationPeriod, roundTickRate, maximumPlayer, minimumPlayer, joinPassword, joinWithInvitation, allowSpectators, roomOwner);
    }
}
