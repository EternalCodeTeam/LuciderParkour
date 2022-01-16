package com.duck.listeners.inventory;

import com.duck.LuciderParkour;
import com.duck.user.User;
import com.duck.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerInventoryDropItemsListener implements Listener {

    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        User user = userManager.getUser(player.getUniqueId()).get();

        if(!player.isOp() || user.getActiveId() > 0)
            event.setCancelled(true);
    }
}
