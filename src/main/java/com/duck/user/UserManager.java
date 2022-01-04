package com.duck.user;

import com.duck.LuciderParkour;
import com.duck.configuration.ConfigurationFactory;
import com.duck.feature.timer.ArenaTimer;
import com.duck.feature.timer.LobbyScoreboardTimer;
import com.duck.feature.timer.TimerType;
import com.duck.utils.BossBarUtils;
import com.duck.utils.ChatUtils;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.bossbar.BossBar;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;
import panda.std.Option;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class UserManager {
    private ConcurrentHashMap<UUID, User> userConcurrentHashMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<UUID, ArenaTimer> arenaTimerConcurrentHashMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<UUID, LobbyScoreboardTimer> lobbyScoreboardTimerConcurrentHashMap = new ConcurrentHashMap<>();

    private final ConfigurationFactory configurationFactory = LuciderParkour.getInstance().getConfigurationFactory();


    public void addLobbyTask(UUID uuid){
        if(!lobbyScoreboardTimerConcurrentHashMap.containsKey(uuid))
            lobbyScoreboardTimerConcurrentHashMap.put(uuid, new LobbyScoreboardTimer(uuid, 1));
    }

    public void removeLobbyTask(UUID uuid){
        lobbyScoreboardTimerConcurrentHashMap.remove(uuid);
    }

    public void removeUser(User user){
        Validate.notNull(user, "User can't be null.");

        userConcurrentHashMap.remove(user.getUuid());
    }

    public void addUser(User user){
        Validate.notNull(user, "User can't be null.");

        if(!userConcurrentHashMap.containsKey(user.getUuid()))
            userConcurrentHashMap.put(user.getUuid(), user);
    }

    public void removeTimer(User user){
        Validate.notNull(user, "User can't be null.");

        arenaTimerConcurrentHashMap.remove(user.getUuid());
    }

    public void addTimer(User user, ArenaTimer arenaTimer){
        Validate.notNull(user, "User can't be null.");
        Validate.notNull(arenaTimer, "Arena timer can't be null.");

        arenaTimerConcurrentHashMap.put(user.getUuid(), arenaTimer);
    }

    public Option<User> getUser(UUID uuid){
        return Option.of(
                userConcurrentHashMap.get(uuid)
        );
    }

    public Option<ArenaTimer> getTimer(UUID uuid){
        return Option.of(
                arenaTimerConcurrentHashMap.get(uuid)
        );
    }

    public void levelUp(User user){
        configurationFactory.getGeneralConfiguration()
                .levelRequirements
                .forEach((key, value) -> {
                    if (user.getLevel() == key && user.getXp() >= value) {
                        user.execute(player ->{
                            int nextLevel = user.getLevel() + 1;

                            player.sendMessage(ChatUtils.component(
                                        configurationFactory.getGeneralConfiguration().standardParkourPrefix + " &7Leveled up to &6" + nextLevel));

                            user.setLevel(user.getLevel() + 1);
                            user.setXp(user.getXp() - value);
                        });
                    }
                });
    }

    public double getXpLeft(User user){
        return configurationFactory.getGeneralConfiguration().levelRequirements
                .get(user.getLevel()) - user.getXp();
    }
}
