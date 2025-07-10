package com.jobmankazakh.smartlog;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SmartLog extends JavaPlugin implements Listener {

    private File logFile;
    private FileConfiguration config;

    private boolean logCreativeTake;
    private boolean logBlockPlace;
    private boolean logBlockBreak;

    private String creativeTakeFormat;
    private String blockPlaceFormat;
    private String blockBreakFormat;

    @Override
    public void onEnable() {
        // Save default config if not exists
        saveDefaultConfig();
        config = getConfig();

        // Load config values
        logCreativeTake = config.getBoolean("logCreativeTake", true);
        logBlockPlace = config.getBoolean("logBlockPlace", true);
        logBlockBreak = config.getBoolean("logBlockBreak", true);

        creativeTakeFormat = config.getString("creativeTakeFormat", "{player},{action},{item},{count},,,");
        blockPlaceFormat = config.getString("blockPlaceFormat", "{player},{action},{item},{count},{x},{y},{z}");
        blockBreakFormat = config.getString("blockBreakFormat", "{player},{action},{item},{count},{x},{y},{z}");

        // Create plugin folder and log file
        File pluginFolder = new File(getDataFolder(), "SmartLog");
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }

        logFile = new File(pluginFolder, "logs.csv");
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
                // Write CSV header
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                    writer.write("PlayerName,Action,ItemOrBlockName,Count,X,Y,Z");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            getLogger().severe("Could not create log file: " + e.getMessage());
        }

        // Register events
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("SmartLog enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SmartLog disabled.");
    }

    @EventHandler
public void onCreativeTake(InventoryCreativeEvent event) {
    if (!logCreativeTake) return;

    if (event.getWhoClicked() instanceof Player player) {
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();

        // The item taken is usually the current item if cursor is empty,
        // or the cursor if player is picking up something
        ItemStack takenItem = null;

        if (current != null && current.getType() != Material.AIR) {
            takenItem = current;
        } else if (cursor != null && cursor.getType() != Material.AIR) {
            takenItem = cursor;
        }

        if (takenItem != null) {
            String logLine = formatLogLine(creativeTakeFormat,
                    player.getName(),
                    "TAKE",
                    takenItem.getType().toString(),
                    takenItem.getAmount(),
                    null);
            writeLogAsync(logLine);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!logBlockPlace) return;

        Player player = event.getPlayer();
        Location loc = event.getBlockPlaced().getLocation();
        Material blockType = event.getBlockPlaced().getType();

        String logLine = formatLogLine(blockPlaceFormat,
                player.getName(),
                "PLACE",
                blockType.toString(),
                1,
                loc);
        writeLogAsync(logLine);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!logBlockBreak) return;

        Player player = event.getPlayer();
        Location loc = event.getBlock().getLocation();
        Material blockType = event.getBlock().getType();

        String logLine = formatLogLine(blockBreakFormat,
                player.getName(),
                "BREAK",
                blockType.toString(),
                1,
                loc);
        writeLogAsync(logLine);
    }

    private String formatLogLine(String format, String player, String action, String item, int count, Location loc) {
        String line = format;
        line = line.replace("{player}", player);
        line = line.replace("{action}", action);
        line = line.replace("{item}", item);
        line = line.replace("{count}", String.valueOf(count));

        if (loc != null) {
            line = line.replace("{x}", String.valueOf(loc.getBlockX()));
            line = line.replace("{y}", String.valueOf(loc.getBlockY()));
            line = line.replace("{z}", String.valueOf(loc.getBlockZ()));
        } else {
            // Remove coordinates placeholders if no location
            line = line.replace("{x}", "");
            line = line.replace("{y}", "");
            line = line.replace("{z}", "");
        }

        // Ensure line ends with newline
        if (!line.endsWith("\n")) {
            line += "\n";
        }
        return line;
    }

    private void writeLogAsync(String logLine) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(logLine);
            } catch (IOException e) {
                getLogger().severe("Failed to write to log file: " + e.getMessage());
            }
        });
    }
  }
