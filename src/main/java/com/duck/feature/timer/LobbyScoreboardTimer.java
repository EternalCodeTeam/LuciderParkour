package com.duck.feature.timer;

import com.duck.LuciderParkour;
import com.duck.feature.scoreboard.ScoreboardManager;
import com.duck.feature.scoreboard.lobby.LobbyScoreboardManager;
import com.duck.user.User;
import com.duck.user.UserManager;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class LobbyScoreboardTimer extends BukkitRunnable {

    private final UUID uuid;
    private final LobbyScoreboardManager lobbyScoreboardManager = LuciderParkour.getInstance().getLobbyScoreboardManager();
    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();

    public LobbyScoreboardTimer(UUID uuid, int seconds){
        this.uuid = uuid;

        runTaskTimerAsynchronously(LuciderParkour.getInstance(), 1L, 20L * seconds);
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void run() {
        User user = userManager.getUser(uuid).get();

        if(user.getActiveId() == 0)
            lobbyScoreboardManager.sendLobbyScoreboard(user);
    }
}
