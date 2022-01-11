package com.duck.listeners.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class PlayerInventoryDragItemsListener implements Listener {

    @EventHandler
    public void onItemDrag(InventoryDragEvent event){
        Player player = (Player) event.getWhoClicked();

        if(!player.isOp())
            event.setCancelled(true);
    }
}
