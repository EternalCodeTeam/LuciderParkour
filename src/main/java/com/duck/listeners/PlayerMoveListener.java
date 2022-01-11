package com.duck.listeners;

import com.duck.LuciderParkour;
import com.duck.configuration.ConfigurationFactory;
import com.duck.feature.timer.ArenaTimer;
import com.duck.feature.timer.TimerType;
import com.duck.parkour.Parkour;
import com.duck.parkour.ParkourManager;
import com.duck.scores.ScoreManager;
import com.duck.user.User;
import com.duck.user.UserManager;
import com.duck.utils.ChatUtils;
import com.duck.utils.LocationUtils;
import com.duck.utils.TimeUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import panda.std.Option;

import java.util.UUID;

public class PlayerMoveListener implements Listener {
    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();
    private final ParkourManager parkourManager = LuciderParkour.getInstance().getParkourManager();
    private final ConfigurationFactory configurationFactory = LuciderParkour.getInstance().getConfigurationFactory();
    private final ScoreManager scoreManager = LuciderParkour.getInstance().getScoreManager();

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();

        User user = userManager.getUser(player.getUniqueId()).get();

        if(user.getActiveId() > 0){
            Parkour parkour = parkourManager.getArena(user.getActiveId()).get();

                checkCurrentStartLocation(player.getUniqueId(), parkour);
                checkCurrentEndLocation(player.getUniqueId(), parkour);
                checkCurrentFailBlock(event, player.getUniqueId(), parkour);
        }
    }

    private void checkCurrentFailBlock(PlayerMoveEvent event, UUID uniqueId, Parkour parkour) {
        String messagePrefix = configurationFactory.getGeneralConfiguration().standardParkourPrefix;

        Player player = Option.of(Bukkit.getPlayer(uniqueId)).get();
        Location location = player.getLocation();

        Block underPlayerBlock = location.getBlock().getRelative(BlockFace.DOWN);
        User user = userManager.getUser(player.getUniqueId()).get();

        if(parkour.getFailBlocks().contains(underPlayerBlock.getType()) && isOnTheEgde(event)){
            Option<ArenaTimer> arenaTimer = userManager.getTimer(player.getUniqueId());

            player.teleport(LocationUtils.asFullLocation(parkour.getSpawnLocation(), ", "));

            if(arenaTimer.isDefined()){

                if(!arenaTimer.get().isCancelled())
                    arenaTimer.get().cancel();

                userManager.removeTimer(user);
            }

            parkourManager.sendParkourScoreboard(player);
        }
    }

    private void checkCurrentEndLocation(UUID uniqueId, Parkour parkour) {
        String messagePrefix = configurationFactory.getGeneralConfiguration().standardParkourPrefix;
        Player player = Option.of(Bukkit.getPlayer(uniqueId)).get();
        Location location = player.getLocation();
        User user = userManager.getUser(player.getUniqueId()).get();

        parkour.getEndLocations().forEach(parsedLocation -> {
            if(LocationUtils.isOnTheLocation(parsedLocation.toNormalLocation(), location)){
                Option<ArenaTimer> arenaTimer = userManager.getTimer(player.getUniqueId());

                double requiredXp = userManager.getXpLeft(user);

                if(arenaTimer.isDefined()){
                    if(!arenaTimer.get().isCancelled())
                        arenaTimer.get().cancel();

                        long time = arenaTimer.get().getElapsedTime();
                        userManager.removeTimer(user);
                        player.sendMessage(ChatUtils.component(messagePrefix + " &7You've completed this arena in &b"
                                + TimeUtils.letterTimeFormat(time)));
                        scoreManager.addScore(user.getUuid(), time, parkour);
                        parkourManager.sendParkourScoreboard(player);

                        user.setXp(user.getXp() + parkour.getXpReward());
                        int nextLevel = user.getLevel() + 1;
                        player.playSound(Sound.sound(
                                Key.key("entity.player.levelup"),
                                Sound.Source.PLAYER,
                                1.0f,
                                1.1f
                        ), Sound.Emitter.self());

                        player.sendMessage(ChatUtils.component(messagePrefix + " &7You've received &6" + parkour.getXpReward() + " &7experience. You need &3" + requiredXp + " &7to levelup to level &3" + nextLevel));
                        userManager.levelUp(user);
                    }
                }
        });

    }

    private void checkCurrentStartLocation(UUID uniqueId, Parkour parkour) {
        String messagePrefix = configurationFactory.getGeneralConfiguration().standardParkourPrefix;
        Player player = Option.of(Bukkit.getPlayer(uniqueId)).get();
        Location location = player.getLocation();
        User user = userManager.getUser(player.getUniqueId()).get();

        parkour.getStartLocations().forEach(parsedLocation -> {
            if(LocationUtils.isOnTheLocation(parsedLocation.toNormalLocation(), location)){
                Option<ArenaTimer> arenaTimer = userManager.getTimer(player.getUniqueId());

                if(!arenaTimer.isDefined()){
                    userManager.addTimer(user, new ArenaTimer(uniqueId, TimerType.EXP_BAR));


                    player.playSound(Sound.sound(
                            Key.key("block.piston.contract"),
                            Sound.Source.BLOCK,
                            1.0f,
                            1.1f
                    ), Sound.Emitter.self());


                    parkourManager.sendParkourScoreboard(player);
                    player.sendMessage(ChatUtils.component(messagePrefix + " &7You've started this arena. Run!"));
                }
            }
        });

    }

    public boolean isOnTheEgde(PlayerMoveEvent event){
        return Math.abs(event.getTo().getZ() - event.getTo().getBlockZ()) > 0.5 /*or whatever value suits you*/ || Math.abs(event.getTo().getX() - event.getTo().getBlockX()) > 0.5;
    }
}
