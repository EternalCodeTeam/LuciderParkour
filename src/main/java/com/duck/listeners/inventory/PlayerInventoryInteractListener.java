package com.duck.listeners.inventory;

import com.duck.LuciderParkour;
import com.duck.configuration.ConfigurationFactory;
import com.duck.feature.gui.GUIManager;
import com.duck.feature.gui.player.PlayerInventoryManager;
import com.duck.parkour.ParkourManager;
import com.duck.user.User;
import com.duck.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import panda.std.Option;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PlayerInventoryInteractListener implements Listener {
    private final ConfigurationFactory configurationFactory = LuciderParkour.getInstance().getConfigurationFactory();
    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();
    private final ParkourManager parkourManager = LuciderParkour.getInstance().getParkourManager();
    private final GUIManager guiManager = LuciderParkour.getInstance().getGuiManager();
    private final PlayerInventoryManager playerInventoryManager = new PlayerInventoryManager();

    @EventHandler
    public void onInventoryItemsClickInteract(PlayerInteractEvent event){
        Action action = event.getAction();
        Player player = event.getPlayer();
        ItemStack clickedItem = event.getItem();
        User user = userManager.getUser(player.getUniqueId()).get();

        if(action.isLeftClick() || action.isRightClick()){
            Option<ItemStack> lobbyItemOption = Option.of(playerInventoryManager.getLobbyItem());
            Option<ItemStack> parkourGuiItemOption = Option.of(playerInventoryManager.getOpenParkourGuiItem());

            if(clickedItem != null){
                checkSelectedItem(event, lobbyItemOption.get(),
                        (itemStack, event1) -> {
                            parkourManager.lobby(configurationFactory, user, player);
                            event1.setCancelled(true);
                        });

                checkSelectedItem(event, parkourGuiItemOption.get(),
                        (itemStack, event1) -> {
                            guiManager.getParkourMainMenu().openInventory(user);
                            event1.setCancelled(true);
                        });
            }
        }
    }

    public void checkSelectedItem(PlayerInteractEvent event, ItemStack itemStack
            , BiConsumer<ItemStack, PlayerInteractEvent> consumer){
        if(Objects.equals(event.getItem(), itemStack))
            consumer.accept(itemStack, event);
    }
}
