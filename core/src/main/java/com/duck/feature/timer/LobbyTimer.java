package com.duck.feature.timer;

import com.duck.LuciderParkour;
import com.duck.parkour.ParkourManager;
import com.duck.user.User;
import com.duck.user.UserManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyTimer extends BukkitRunnable {
    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();
    private final ParkourManager parkourManager = LuciderParkour.getInstance().getParkourManager();
    private final User user;


    public LobbyTimer(int seconds, Plugin plugin, User user) {
        this.user = user;

        runTaskTimer(plugin, 1L, 20L * seconds);
    }

    @Override
    public void run() {
        if(user.getActiveId() == 0)
            user.execute(parkourManager::sendLobbyScoreboard);
        else if(!isCancelled()) cancel();
    }
}

