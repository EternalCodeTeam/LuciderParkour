package com.duck.utils;

import com.duck.parkour.Parkour;
import com.duck.scores.ParkourScore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import panda.std.Option;

import java.util.Comparator;
import java.util.List;

public final class ScoreUtils {
    public static Player getFirstPlayerScore(Parkour parkour){
        List<ParkourScore> scoreList = parkour.getParkourScores()
                .stream()
                .sorted(Comparator.comparingLong(ParkourScore::getTime))
                .toList();

        return Option.of(Bukkit.getPlayer(scoreList.get(0).getUuid())).get();
    }

    public static long getFirstPlayerTime(Parkour parkour){
        List<ParkourScore> scoreList = parkour.getParkourScores()
                .stream()
                .sorted(Comparator.comparingLong(ParkourScore::getTime))
                .toList();

        return scoreList.get(0).getTime();
    }
}
