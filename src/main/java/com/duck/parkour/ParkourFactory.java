package com.duck.parkour;

import com.duck.LuciderParkour;
import com.duck.configuration.ConfigurationFactory;
import com.duck.configuration.parsers.ParsedLocation;
import com.duck.user.User;
import com.duck.user.UserManager;
import com.duck.utils.ChatUtils;
import com.duck.utils.LocationUtils;
import com.google.common.collect.HashBiMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import panda.std.Option;
import panda.std.Result;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ParkourFactory{
    private final ParkourManager parkourManager = LuciderParkour.getInstance().getParkourManager();
    private final ConfigurationFactory configurationFactory = LuciderParkour.getInstance().getConfigurationFactory();


    public Result<Parkour, String> createArena(User user, int id){
        String prefix = configurationFactory.getGeneralConfiguration()
                .standardParkourPrefix;

        Option<Parkour> parkour = parkourManager.getArena(id);


        if(!parkour.isEmpty()) {
            user.execute(player -> player.sendMessage(ChatUtils.component(prefix + " &7Parkour already exist!")));

            return Result.error("Parkour already exist.");
        }

        return Result.<Parkour, String>ok(parkour.orElse(
                Parkour.builder()
                        .id(id)
                        .name("Parkour" + id)
                        .failBlocks(new ArrayList<>())
                        .spawnLocation(
                                LocationUtils.fromFullLocation(
                                        new Location(Bukkit.getWorld("world"), 1, 1, 1, 1, 1),
                                        ", "
                                )
                        )
                        .startLocations(new ArrayList<>())
                        .endLocations(new ArrayList<>())
                        .parkourScores(new ArrayList<>())
                        .xpReward(0)
                        .levelRequired(1)
                        .build()
        ).get()).peek(parkour1 -> {
            parkourManager.addArena(parkour1);

            user.execute(player ->
                    player.sendMessage(ChatUtils.component(prefix + " &7Parkour has been created! Id= &6" + id)));
        });
    }

    public void removeArena(int id, User user){
        String prefix = configurationFactory.getGeneralConfiguration()
                .standardParkourPrefix;


        Option<Parkour> parkour = parkourManager.getArena(id);

        if(parkour.isEmpty())
            user.execute(player -> player.sendMessage(ChatUtils.component(prefix + " &7parkour is not exist.")));

        parkour.peek(parkour1 -> {
            parkourManager.removeArena(parkour1);

            user.execute(player -> player.sendMessage(ChatUtils.component(prefix + " &7Parkour has been removed! Id= &6" + id)));
        });
    }
}
