package com.duck.listeners.inventory;

import com.duck.LuciderParkour;
import com.duck.user.User;
import com.duck.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import panda.std.Option;


public class PlayerInventoryClickListener implements Listener {

    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();


    @EventHandler
    public void onItemsClick(InventoryClickEvent event)
    {
        Option<Inventory> clickedInventory = Option.of(event.getClickedInventory());

        if(clickedInventory.isDefined() && clickedInventory.get().getType() == InventoryType.PLAYER){
            
            Player player = (Player) event.getWhoClicked();
            User user = userManager.getUser(player.getUniqueId()).get();
            
            if(!player.isOp() || user.getActiveId() > 0)
                event.setCancelled(true);
        }
    }
}
