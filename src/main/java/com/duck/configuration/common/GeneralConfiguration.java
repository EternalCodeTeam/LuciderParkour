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
            .build();

    @Description("# Default parkour scoreboard title")
    public String parkourScoreboardTitle = "&fTop %top-value-size% on %top-value-arena%.";

    @Description("# Default parkour scoreboard score")
    public String parkourScoreboardScore = "&7%user-top-position%. &e%user-top-name% &a%user-top-time%";

    @Description("# Lobby location")
    public String lobbyLocation = LocationUtils.fromFullLocation(new Location(Bukkit.getWorld("world"), 1, 1, 1, 1, 1), ", ");

    @Description("# Unknown command message")
    public String unknownCommandMessage = "&8(&4&l!&8) &7Unknown command argument!";

    @Description("# Command argument valid use")
    public String validCommandArgumentUse= "&8(&4!&8) &7Wrong argument format use. Please, try &8(&6%use-arg%&8)";
}
