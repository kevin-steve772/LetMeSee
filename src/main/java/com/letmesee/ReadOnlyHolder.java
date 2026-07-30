package com.letmesee;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ReadOnlyHolder implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        return null;
    }
}