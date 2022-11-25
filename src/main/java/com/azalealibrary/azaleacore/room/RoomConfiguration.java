package com.azalealibrary.azaleacore.room;

import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.configuration.property.AssignmentPolicy;
import com.azalealibrary.azaleacore.foundation.configuration.property.Property;
import com.azalealibrary.azaleacore.foundation.configuration.property.primitive.BooleanProperty;
import com.azalealibrary.azaleacore.foundation.configuration.property.primitive.IntegerProperty;
import com.azalealibrary.azaleacore.foundation.configuration.property.primitive.StringProperty;
import org.bukkit.Bukkit;

import java.util.List;

public class RoomConfiguration implements Configurable {

    // required
    private final IntegerProperty roundGracePeriod = new IntegerProperty("roundGracePeriod", 3, true, AssignmentPolicy.POSITIVE_INTEGER);
    private final IntegerProperty roundDurationPeriod = new IntegerProperty("roundDurationPeriod", 30, true, AssignmentPolicy.POSITIVE_INTEGER);
    private final IntegerProperty roundTickRate = new IntegerProperty("roundTickRate", 1, true, AssignmentPolicy.create(value -> value > 0 && value <= Bukkit.getServer().getMaxPlayers()));
    private final IntegerProperty minimumPlayer = new IntegerProperty("minimumPlayer", 2, true, AssignmentPolicy.POSITIVE_INTEGER);
    private final IntegerProperty maximumPlayer = new IntegerProperty("maximumPlayer", 4, true, AssignmentPolicy.create(value -> value <= 100));

    // optional
    private final StringProperty joinPassword = new StringProperty("joinPassword", null, false);
    private final BooleanProperty joinWithInvitation = new BooleanProperty("joinWithInvitation", false, false);

    public int getRoundGracePeriod() {
        return roundGracePeriod.get();
    }

    public int getRoundDurationPeriod() {
        return roundDurationPeriod.get();
    }

    public int getRoundTickRate() {
        return roundTickRate.get();
    }

    public int getMinimumPlayer() {
        return minimumPlayer.get();
    }

    public int getMaximumPlayer() {
        return maximumPlayer.get();
    }

    public String getJoinPassword() {
        return joinPassword.get();
    }

    public boolean getJoinWithInvitation() {
        return joinWithInvitation.get();
    }

    @Override
    public List<Property<?>> getProperties() {
        return List.of(roundGracePeriod, roundDurationPeriod, roundTickRate, minimumPlayer, maximumPlayer, joinPassword, joinWithInvitation);
    }
}
