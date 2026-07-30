package com.letmesee;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

public class InventoryListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        if (!isReadOnly(event.getView().getTopInventory().getHolder())) return;

        if (event.getClickedInventory() == null ||
            isReadOnly(event.getClickedInventory().getHolder())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDrag(InventoryDragEvent event) {
        if (!isReadOnly(event.getView().getTopInventory().getHolder())) return;

        InventoryView view = event.getView();
        Inventory topInv = view.getTopInventory();
        for (int rawSlot : event.getRawSlots()) {
            if (view.getInventory(rawSlot) != null &&
                isReadOnly(view.getInventory(rawSlot).getHolder())) {
                event.setCancelled(true);
                return;
            }
        }
    }

    private boolean isReadOnly(InventoryHolder holder) {
        return holder instanceof ReadOnlyHolder;
    }
}