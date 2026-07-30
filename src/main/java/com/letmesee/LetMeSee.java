package com.letmesee;

import org.bukkit.plugin.java.JavaPlugin;

public class LetMeSee extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("lms").setExecutor(new LMSCommand(this));
        getCommand("lms").setTabCompleter(new LMSTabCompleter());
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getLogger().info("LetMeSee 已启用");
    }

    @Override
    public void onDisable() {
        getLogger().info("LetMeSee 已禁用");
    }
}