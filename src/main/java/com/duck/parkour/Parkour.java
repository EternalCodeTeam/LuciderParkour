package com.duck.parkour;

import com.duck.configuration.parsers.ParsedLocation;
import com.duck.scores.ParkourScore;
import com.duck.utils.LocationUtils;
import lombok.*;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Parkour {
    private int id;
    private String name;

    private List<Material> failBlocks;
    private List<ParsedLocation> startLocations;
    private List<ParsedLocation> endLocations;

    private String spawnLocation;

    private List<ParkourScore> parkourScores;

    private double xpReward;
    private int levelRequired;
}
