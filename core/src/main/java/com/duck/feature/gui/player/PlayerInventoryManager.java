package com.duck.feature.gui.player;

import com.duck.utils.ChatUtils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryManager {
    private ItemStack lobbyItem;
    private ItemStack openParkourGuiItem;

    public void buildItems(){
        this.lobbyItem = ItemBuilder.from(Material.CLOCK)
                .amount(1)
                .name(ChatUtils.component("&7> &3Lobby Item"))
                .build();

        this.openParkourGuiItem = ItemBuilder.from(Material.MINECART)
                .amount(1)
                .name(ChatUtils.component("&7> &6Parkour Selector"))
                .build();
    }

    public ItemStack getLobbyItem(){
        buildItems();
        return lobbyItem;
    }

    public ItemStack getOpenParkourGuiItem(){
        buildItems();
        return openParkourGuiItem;
    }

    public void setupInventory(Player player){
        buildItems();
        player.getInventory().clear();

        player.getInventory().setItem(0, openParkourGuiItem);
        player.getInventory().setItem(8, lobbyItem);
    }
}
