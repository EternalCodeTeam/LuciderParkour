package com.duck.feature.timer;

import com.duck.LuciderParkour;
import com.duck.configuration.parsers.ParsedLocation;
import com.duck.parkour.Parkour;
import com.duck.parkour.ParkourManager;
import com.duck.user.User;
import com.duck.user.UserManager;
import org.bukkit.Particle;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class ParticlesTimer extends BukkitRunnable {
    private final ParkourManager parkourManager = LuciderParkour.getInstance().getParkourManager();
    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();


    public ParticlesTimer(long time, Plugin plugin){



        runTaskTimerAsynchronously(plugin, 1L, time);
    }

    @Override
    public void run() {
        userManager
                .getUserConcurrentHashMap()
                .values()
                .stream()
                .filter(user -> user.getActiveId() > 0)
                .forEach(user -> {

                    Parkour parkour = parkourManager.getArena(user.getActiveId()).get();

                    if(parkour != null) {

                        for (ParsedLocation startLocation : parkour.getStartLocations()) {

                            user.execute(player -> player.spawnParticle(
                                    Particle.PORTAL,
                                    startLocation.getX(),
                                    startLocation.getY() + 1,
                                    startLocation.getZ(),
                                    40,
                                    .200,
                                    .400,
                                    .200,
                                    0.2
                            ));
                        }

                        for (ParsedLocation endLocation : parkour.getEndLocations()) {

                            user.execute(player -> player.spawnParticle(
                                    Particle.PORTAL,
                                    endLocation.getX(),
                                    endLocation.getY() + 1,
                                    endLocation.getZ(),
                                    40,
                                    .200,
                                    .550,
                                    .200,
                                    0.2
                            ));
                        }
                    }
                });
    }
}
