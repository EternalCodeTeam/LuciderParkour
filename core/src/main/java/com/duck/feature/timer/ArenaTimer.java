package com.duck.feature.timer;

import com.duck.LuciderParkour;
import com.duck.utils.ChatUtils;
import com.duck.utils.TimeUtils;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import panda.std.Option;

import java.util.UUID;
import java.util.function.Consumer;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ArenaTimer extends BukkitRunnable {

    private long time;
    private UUID uuid;
    private TimerType timerType;

    private Player player;

    public ArenaTimer(UUID uuid, TimerType timerType){
        this.time = System.currentTimeMillis();

        this.timerType = timerType;
        this.player = Bukkit.getPlayer(uuid);

        runTaskTimerAsynchronously(LuciderParkour.getInstance(), 1L, 1L);
    }

    @Override
    public void run() {
        TimerType currentTimerType = Option.of(timerType).get();


        long timePassed = System.currentTimeMillis() - time;

        Consumer<UUID> acceptTimerWithType = switch(currentTimerType)
                {
                    case EXP_BAR -> timerType -> {
                        long millis = timePassed % 1000;

                        float expTime = millis / 1000f;
                        int seconds = (int) (timePassed / 1000L % 60);

                        player.setExp(expTime);
                        player.setLevel(seconds);
                    };

                    case ACTION_BAR -> timerType -> {
                        String formattedTime = TimeUtils.letterTimeFormat(timePassed);

                        player.sendActionBar(ChatUtils.component("&7> &b" + formattedTime));
                    };

                    default -> null;
                };

        assert acceptTimerWithType != null;
        acceptTimerWithType.accept(uuid);
    }

    public long getElapsedTime(){
        return System.currentTimeMillis() - time;
    }
}
