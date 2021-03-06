package com.duck.listeners;

import com.duck.LuciderParkour;
import com.duck.configuration.ConfigurationFactory;
import com.duck.feature.gui.player.PlayerInventoryManager;
import com.duck.feature.timer.LobbyTimer;
import com.duck.user.User;
import com.duck.user.UserManager;
import com.duck.utils.ChatUtils;
import com.duck.utils.LocationUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import panda.std.Option;

import java.util.ArrayList;

public class PlayerJoinListener implements Listener {

    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();
    private final ConfigurationFactory configurationFactory = LuciderParkour.getInstance().getConfigurationFactory();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        String prefix = configurationFactory.getGeneralConfiguration().standardParkourPrefix;

        Option<User> user = userManager.getUser(player.getUniqueId());

        if(!user.isDefined()) {
            userManager.addUser(
                    User.builder()
                            .uuid(player.getUniqueId())
                            .xp(0)
                            .level(1)
                            .scoresLimit(10)
                            .completedArenasIds(new ArrayList<>())
                            .username(player.getName())
                            .coins(0)
                            .activeId(0)
                            .friends(new ArrayList<>())
                            .build()
            );

            if(configurationFactory.getGeneralConfiguration().lobbyLocation != null)
                player.teleport(LocationUtils.asFullLocation(configurationFactory.getGeneralConfiguration().lobbyLocation, ", "));

            player.sendMessage(ChatUtils.component(prefix + " &7Welcome, &b" + player.getName()));
        }else {
            player.sendMessage(ChatUtils.component(prefix + " &7Welcome back, &b" + player.getName()));
            new LobbyTimer(1, LuciderParkour.getInstance(), user.get());
            player.setGameMode(GameMode.ADVENTURE);
            new PlayerInventoryManager().setupInventory(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        Option<User> user = userManager.getUser(player.getUniqueId());

        user.get().setActiveId(0);
        userManager.removeTimer(user.get());
    }
}
