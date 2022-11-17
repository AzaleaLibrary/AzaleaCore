package com.azalealibrary.azaleacore.room;

import com.azalealibrary.azaleacore.api.core.ConfigurableProperty;
import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import org.bukkit.Location;
import org.bukkit.WeatherType;

import java.util.List;

public class PlaygroundConfiguration implements Configurable {

    private final ConfigurableProperty<Location> spawn = ConfigurableProperty.location("join.spawn", null).build();
    private final ConfigurableProperty<Location> borderOrigin = ConfigurableProperty.location("border.origin", spawn.get()).build();
    private final ConfigurableProperty<Integer> borderRadius = ConfigurableProperty.integer("border.radius", 50).build();
    private final ConfigurableProperty<WeatherType> weather = ConfigurableProperty.create("weather", WeatherType.CLEAR).build();
    private final ConfigurableProperty<Integer> time = ConfigurableProperty.integer("time", 6000).build();

    public Location getSpawn() {
        return spawn.get();
    }

    public Location getBorderOrigin() {
        return borderOrigin.get();
    }

    public Integer getBorderRadius() {
        return borderRadius.get();
    }

    public WeatherType getWeather() {
        return weather.get();
    }

    public Integer getTime() {
        return time.get();
    }

    @Override
    public String getConfigName() {
        return "unused"; // TODO - review
    }

    @Override
    public List<ConfigurableProperty<?>> getProperties() {
        return List.of(spawn, borderOrigin, borderRadius, weather, time);
    }
}
