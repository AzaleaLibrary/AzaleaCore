package com.azalealibrary.azaleacore.room;

import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.configuration.ConfigurableProperty;
import org.bukkit.WeatherType;
import org.bukkit.util.Vector;

import java.util.List;

public class PlaygroundConfiguration implements Configurable {

    private final ConfigurableProperty<Vector> spawn = ConfigurableProperty.location("join.spawn", new Vector()).build();
    private final ConfigurableProperty<Vector> borderOrigin = ConfigurableProperty.location("border.origin", spawn.get()).build();
    private final ConfigurableProperty<Integer> borderRadius = ConfigurableProperty.integer("border.radius", 50).build();
    private final ConfigurableProperty<WeatherType> weather = ConfigurableProperty.create("weather", WeatherType.CLEAR).build();
    private final ConfigurableProperty<Integer> time = ConfigurableProperty.integer("time", 6000).build();

    public Vector getSpawn() {
        return spawn.get();
    }

    public Vector getBorderOrigin() {
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
    public List<ConfigurableProperty<?>> getProperties() {
        return List.of(spawn, borderOrigin, borderRadius, weather, time);
    }
}
