package com.duck.user;

import com.duck.feature.timer.TimerType;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import panda.std.Option;

import java.lang.management.PlatformLoggingMXBean;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private UUID uuid;
    private String username;


    private int level;
    private double xp;
    private double coins;
    private int activeId;

    private List<Integer> completedArenasIds;

    private int scoresLimit;

    private boolean highScoresScoreboardVisibility;

    private int prefixIndex;
    private TimerType timerType;

    private List<User> friends;

    public void execute(Consumer<Player> playerConsumer){
        Option<Player> player = Option.of(Bukkit.getPlayer(uuid));

        if(player.isDefined()) {
            playerConsumer.accept(player.get());
        }
    }

    public Player toPlayer(){
        return Option.of(Bukkit.getPlayer(uuid)).get();
    }

}
