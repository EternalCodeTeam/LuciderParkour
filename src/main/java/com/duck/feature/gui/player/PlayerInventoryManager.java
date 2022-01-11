package com.duck.feature.gui.player;

import com.duck.utils.ChatUtils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryManager {
    private ItemStack lobbyItem;
    private ItemStack openParkourGuiItem;

    public void buildItems(){
        this.lobbyItem = ItemBuilder.from(Material.COMPASS)
                .amount(1)
                .name(ChatUtils.component("&7> &3Lobby Item"))
                .build();

        this.openParkourGuiItem = ItemBuilder.from(Material.MINECART)
                .amount(1)
                .name(ChatUtils.component("&7 &6Parkour Selector"))
                .build();
    }

    public ItemStack getLobbyItem(){
        return lobbyItem;
    }

    public ItemStack getOpenParkourGuiItem(){
        return openParkourGuiItem;
    }

    public void setupInventory(Player player){
        buildItems();
        player.getInventory().clear();

        player.getInventory().setItem(36, openParkourGuiItem);
        player.getInventory().setItem(44, lobbyItem);
    }
}
