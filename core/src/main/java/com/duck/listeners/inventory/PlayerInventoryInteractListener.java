package com.duck.listeners.inventory;

import com.duck.LuciderParkour;
import com.duck.configuration.ConfigurationFactory;
import com.duck.feature.gui.GUIManager;
import com.duck.feature.gui.player.PlayerInventoryManager;
import com.duck.parkour.ParkourManager;
import com.duck.user.User;
import com.duck.user.UserManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerInventoryInteractListener implements Listener {
    private final ConfigurationFactory configurationFactory = LuciderParkour.getInstance().getConfigurationFactory();
    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();
    private final ParkourManager parkourManager = LuciderParkour.getInstance().getParkourManager();
    private final GUIManager guiManager = LuciderParkour.getInstance().getGuiManager();
    private final PlayerInventoryManager playerInventoryManager = new PlayerInventoryManager();

    private final Map<UUID, Boolean> clickedItem = new HashMap<>();


    @EventHandler
    public void onInventoryItemsClickInteract(PlayerInteractEvent event){
        Action action = event.getAction();
        Player player = event.getPlayer();

        User user = userManager.getUser(player.getUniqueId()).get();

        ItemStack openGuiItem = playerInventoryManager.getOpenParkourGuiItem();
        ItemStack lobbyItem = playerInventoryManager.getLobbyItem();


        if(player.getInventory().getItemInMainHand().getType() != Material.AIR){
            switch(action){
                case LEFT_CLICK_AIR, RIGHT_CLICK_AIR -> {
                    executeIsEquals(player, openGuiItem,
                        player1 -> guiManager.getParkourMainMenu().openInventory(user));

                    executeIsEquals(player, lobbyItem,
                        player1 -> parkourManager.lobby(configurationFactory, user, player1));
                }

                case LEFT_CLICK_BLOCK, RIGHT_CLICK_BLOCK -> {
                    if(event.getClickedBlock() != null && event.getClickedBlock().getType() != Material.AIR){
                        executeIsEquals(player, openGuiItem,
                            player1 -> guiManager.getParkourMainMenu().openInventory(user));

                        executeIsEquals(player, lobbyItem,
                            player1 -> parkourManager.lobby(configurationFactory, user, player1));
                    }
                }
            }
        }
    }

    public void executeIsEquals(Player player, ItemStack itemStack, Consumer<Player> playerConsumer){
        if(player.getInventory().getItemInMainHand().isSimilar(itemStack)){
            if(!clickedItem.containsKey(player.getUniqueId())) {
                clickedItem.put(player.getUniqueId(), true);
                playerConsumer.accept(player);
            }else {
                clickedItem.replace(player.getUniqueId(), true);
                playerConsumer.accept(player);
            }

            clickedItem.replace(player.getUniqueId(), false);


        }
    }

}
