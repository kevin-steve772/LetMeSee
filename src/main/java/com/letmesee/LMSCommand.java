package com.letmesee;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

public class LMSCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public LMSCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c只有玩家可以使用此命令");
            return true;
        }

        if (!player.hasPermission("letmesee.use")) {
            player.sendMessage("§c你没有权限使用此命令");
            return true;
        }

        if (args.length < 4) {
            player.sendMessage("§c用法: /lms <世界> <X> <Y> <Z>");
            return true;
        }

        World world = Bukkit.getWorld(args[0]);
        if (world == null) {
            player.sendMessage("§c未找到世界: " + args[0]);
            return true;
        }

        int x, y, z;
        try {
            x = Integer.parseInt(args[1]);
            y = Integer.parseInt(args[2]);
            z = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            player.sendMessage("§c坐标必须为整数");
            return true;
        }

        Location targetLocation = new Location(world, x, y, z);

        // 在目标区域线程执行块操作 (Folia兼容)
        Bukkit.getRegionScheduler().run(plugin, targetLocation, task -> {
            Block block = targetLocation.getBlock();
            BlockState state = block.getState();

            if (!(state instanceof Container container)) {
                player.sendMessage("§c该位置没有容器");
                return;
            }

            String containerName = container.getCustomName();
            if (containerName == null || containerName.isEmpty()) {
                containerName = getContainerDisplayName(block);
            }

            Inventory targetInv = container.getInventory();
            InventoryType type = targetInv.getType();

            Inventory viewInv;
            if (type == InventoryType.CHEST) {
                viewInv = Bukkit.createInventory(
                    new ReadOnlyHolder(), targetInv.getSize(),
                    "§7[只读] " + containerName
                );
            } else {
                viewInv = Bukkit.createInventory(
                    new ReadOnlyHolder(), type,
                    "§7[只读] " + containerName
                );
            }
            viewInv.setContents(targetInv.getContents());

            player.openInventory(viewInv);
            player.sendMessage("§a已打开 " + containerName + " 的只读视图");
        });

        return true;
    }

    private String getContainerDisplayName(Block block) {
        return switch (block.getType()) {
            case CHEST -> "箱子";
            case TRAPPED_CHEST -> "陷阱箱";
            case BARREL -> "木桶";
            case SHULKER_BOX, WHITE_SHULKER_BOX, ORANGE_SHULKER_BOX,
                 MAGENTA_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX,
                 YELLOW_SHULKER_BOX, LIME_SHULKER_BOX, PINK_SHULKER_BOX,
                 GRAY_SHULKER_BOX, LIGHT_GRAY_SHULKER_BOX, CYAN_SHULKER_BOX,
                 PURPLE_SHULKER_BOX, BLUE_SHULKER_BOX, BROWN_SHULKER_BOX,
                 GREEN_SHULKER_BOX, RED_SHULKER_BOX, BLACK_SHULKER_BOX ->
                    "潜影盒";
            case FURNACE -> "熔炉";
            case BLAST_FURNACE -> "高炉";
            case SMOKER -> "烟熏炉";
            case HOPPER -> "漏斗";
            case DROPPER -> "投掷器";
            case DISPENSER -> "发射器";
            case BREWING_STAND -> "酿造台";
            default -> block.getType().name();
        };
    }
}