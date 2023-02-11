package net.azalealibrary.azaleacore.command;

import net.azalealibrary.azaleacore.AzaleaCore;
import net.azalealibrary.azaleacore.foundation.AzaleaException;
import net.azalealibrary.azaleacore.foundation.message.ChatMessage;
import net.azalealibrary.azaleacore.manager.TeleporterManager;
import net.azalealibrary.azaleacore.teleport.Teleporter;
import net.azalealibrary.azaleacore.util.ScheduleUtil;
import net.azalealibrary.azaleacore.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class TeleporterCommand extends CommandNode {

    public TeleporterCommand() {
        super("@teleporter",
                new Create(),
                new Delete(),
                new Reveal()
        );
    }

    private static final class Create extends CommandNode {

        public Create() {
            super("create");
        }

        @Override
        public List<String> complete(CommandSender sender, Arguments arguments) {
            if (arguments.size() == 1) {
                return List.of("<name>");
            } else if (arguments.size() == 2) {
                return Bukkit.getServer().getWorlds().stream().map(World::getName).toList();
            }
            return new ArrayList<>();
        }

        @Override
        public void execute(CommandSender sender, Arguments arguments) {
            String name = arguments.notMissing(0, "name");
            Location to = arguments.find(1, "destination", Bukkit::getWorld).getSpawnLocation();
            Block block = ((Player) sender).getTargetBlock(null, 10);

            if (block.isEmpty()) {
                throw new AzaleaException("Target position is not a block");
            }

            Teleporter teleporter = TeleporterManager.getInstance().create(name, block.getLocation() , to);
            ChatMessage.info("Created " + TextUtil.getName(teleporter) + " teleporter.").post(AzaleaCore.PLUGIN_ID, sender);
        }
    }

    private static final class Delete extends CommandNode {

        public Delete() {
            super("delete");
        }

        @Override
        public List<String> complete(CommandSender sender, Arguments arguments) {
            return arguments.size() == 1 ? TeleporterManager.getInstance().getKeys() : new ArrayList<>();
        }

        @Override
        public void execute(CommandSender sender, Arguments arguments) {
            Teleporter teleporter = arguments.find(0, "teleporter", TeleporterManager.getInstance()::get);
            TeleporterManager.getInstance().remove(teleporter);
            ChatMessage.info("Deleted " + TextUtil.getName(teleporter) + " teleporter.").post(AzaleaCore.PLUGIN_ID, sender);
        }
    }

    private static final class Reveal extends CommandNode {

        public Reveal() {
            super("reveal");
        }

        @Override
        public void execute(CommandSender sender, Arguments arguments) {
            World world = ((Player) sender).getWorld();
            List<FallingBlock> entities = new ArrayList<>();

            for (Teleporter teleporter : TeleporterManager.getInstance().getAll()) {
                Location location = teleporter.getPosition();

                if (location.getWorld() == world) {
                    Block block = world.getBlockAt(location);
                    FallingBlock entity = world.spawnFallingBlock(location, block.getBlockData());
                    entity.setVelocity(new Vector(0, 0, 0));
                    entity.setCustomName(teleporter.getName());
                    entity.setCustomNameVisible(true);
                    entity.setHurtEntities(false);
                    entity.setGravity(false);
                    entity.setGlowing(true);
                    entity.setDropItem(false);
                    entity.setInvulnerable(true);
                    entities.add(entity);
                }
            }

            String message = "Showing " + ChatColor.YELLOW + entities.size() + ChatColor.RESET + " teleporters.";
            ChatMessage.info(message).post(AzaleaCore.PLUGIN_ID, sender);

            ScheduleUtil.doDelayed(20 * 3, () -> {
                for (FallingBlock entity : entities) {
                    world.setBlockData(entity.getLocation(), entity.getBlockData());
                    entity.remove();
                }
//                ChatMessage.info("Toggled teleporter visibility off.").post(AzaleaCore.PLUGIN_ID, sender);
            });
        }
    }
}
