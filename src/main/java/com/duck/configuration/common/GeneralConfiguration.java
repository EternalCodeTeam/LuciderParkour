package com.duck.configuration.common;

import com.duck.utils.LocationUtils;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.dzikoysk.cdn.entity.Description;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.checkerframework.checker.fenum.qual.SwingElementOrientation;

import java.io.Serializable;
import java.util.Map;

public class GeneralConfiguration implements Serializable {

    @Description("# Maximum level that user can get.")
    public int maximumUserLevel = 50;

    @Description("# if you want to use custom player join message, then change false to true.")
    public boolean useCustomJoinMessage = false;

    @Description("# Standard parkour prefix.")
    public String standardParkourPrefix = "&6[&3PK&6]";

    @Description("#==========================")
    @Description("    Level requirements.   ")
    @Description("#==========================")
    public Map<Integer, Double> levelRequirements = ImmutableMap.<Integer, Double>builder()
            .put(1, 25.5)
            .put(2, 54.5)
            .put(3, 80.5)
            .put(4, 102.5)
            .put(5, 155.0)
            .put(6, 240.0)
            .put(7, 425.0)
            .put(8, 650.0)
            .put(9, 800.0)
            .put(10, 1250.0)
            .put(11, 1550.0)
            .put(12, 2000.0)
            .put(13, 3500.0)
            .put(14, 3900.0)
            .put(15, 4500.0)
            .put(16, 5600.0)
            .put(17, 7600.0)
            .put(18, 9203.0)
            .put(19, 10930.0)
            .put(20, 11230.0)
            .put(21, 12505.0)
            .put(22, 13542.0)
            .put(23, 14503.0)
            .put(24, 15503.0)
            .put(25, 17503.0)
            .put(26, 19000.0)
            .put(27, 21402.0)
            .put(28, 24302.0)
            .put(29, 25202.0)
            .put(30, 28503.0)
            .put(31, 31203.0)
            .build();

    @Description("# Admin permissions")
    public String adminPermission = "luciderparkour.admin";

    @Description("# User permissions")
    public String userPermission = "luciderparkour.user";

    @Description("# Lobby location")
    public String lobbyLocation = LocationUtils.fromFullLocation(new Location(Bukkit.getWorld("world"), 1, 1, 1, 1, 1), ", ");

    @Description("# Unknown command message")
    public String unknownCommandMessage = "&8(&4&l!&8) &7Unknown command argument!";

    @Description("# Command argument valid use")
    public String validCommandArgumentUse= "&8(&4!&8) &7Wrong argument format use. Please, try &8(&6%use-arg%&8)";
}
