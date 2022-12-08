package com.azalealibrary.azaleacore.manager;

import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.registry.MinigameIdentifier;
import com.azalealibrary.azaleacore.minigame.Minigame;
import com.azalealibrary.azaleacore.playground.Playground;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Objects;

public class PlaygroundManager extends Manager<Playground> {

    private static final PlaygroundManager MANAGER = new PlaygroundManager();

    public static PlaygroundManager getInstance() {
        return MANAGER;
    }

    private PlaygroundManager() {
        super("playground");
    }

    public @Nullable Playground get(Player player) {
        return getAll().stream().filter(p -> p.getWorld().getPlayers().contains(player)).findFirst().orElse(null);
    }

    public Playground create(String name, MinigameIdentifier identifier, File map) {
        File playgroundDir = FileUtil.getPlayground(name);

        if (getAll().size() >= AzaleaConfiguration.getInstance().getMaxPlaygroundCount()) {
            throw new AzaleaException("Max playground count reached!");
        } else if (exists(name)) {
            throw new AzaleaException("Playground '" + name + "' already exists.");
        }

        FileUtil.copyDirectory(map, playgroundDir);
        FileUtil.delete(new File(playgroundDir, "uid.dat")); // otherwise may spigot complains

        WorldCreator creator = new WorldCreator(playgroundDir.getPath().replaceFirst("./", ""));
        World world = Bukkit.createWorld(creator);

        Minigame minigame = Minigame.create(identifier);
        Playground playground = new Playground(name, world, minigame);
        add(name, playground);
        return playground;
    }

    public void delete(Playground playground) {
        playground.setParty(null);
        for (Player player : playground.getWorld().getPlayers()) {
            player.teleport(AzaleaConfiguration.getInstance().getServerLobby().getSpawnLocation());
        }

        Bukkit.unloadWorld(playground.getWorld(), false);
        FileUtil.delete(FileUtil.getPlayground(playground.getName()));
        remove(playground);
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        super.deserialize(configuration);

        for (File file : FileUtil.getPlaygrounds()) {
            if (getKeys().stream().noneMatch(key -> key.equals(file.getName()))) {
                System.out.println("Found stray room '" + file.getName() + "'.");

                if (AzaleaConfiguration.getInstance().shouldRemoveStrayRooms()) {
                    System.out.println("Removing room '" + file.getName() + "'.");
                    FileUtil.delete(file);
                }
            }
        }
    }

    @Override
    protected void serializeEntry(ConfigurationSection section, Playground playground) {
        section.set("name", playground.getName());
        section.set("world", playground.getWorld().getName());
        section.set("minigame", playground.getMinigame().getIdentifier().toString());
        playground.getMinigame().serialize(section.createSection("configs"));
    }

    @Override
    protected Playground deserializeEntry(ConfigurationSection section) {
        String name = Objects.requireNonNull(section.getString("name"));
        World world = Bukkit.getWorld(Objects.requireNonNull(section.getString("world")));
        String identifier = Objects.requireNonNull(section.getString("minigame"));
        Minigame minigame = Minigame.create(new MinigameIdentifier(identifier));
        minigame.deserialize(Objects.requireNonNull(section.getConfigurationSection("configs")));
        return new Playground(name, world, minigame);
    }
}
