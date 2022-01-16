package com.duck.listeners;

import com.duck.LuciderParkour;
import com.duck.user.User;
import com.duck.user.UserManager;
import com.duck.utils.ChatUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();

    @EventHandler
    public void onChat(AsyncChatEvent event){
        Player player = event.getPlayer();

        User user = userManager.getUser(player.getUniqueId()).get();

        event.viewers()
                .forEach(viewer -> viewer.sendMessage(ChatUtils.component("&8(&b" + user.getLevel() + "&8) &7" + user.getUsername() + ": &f" + ChatUtils.asString(event.message()))));

        event.setCancelled(true);
    }
}
