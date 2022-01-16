package com.duck.utils;

import com.duck.configuration.parsers.ParsedLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class LocationUtils {

    public static Location asNormalizedLocation(String locationString, String regex){
        String[] strings = locationString.split(regex);

        return new Location(
                Bukkit.getWorld(strings[0]),
                Double.parseDouble(strings[1]),
                Double.parseDouble(strings[2]),
                Double.parseDouble(strings[3])
        );
    }

    public static Location asFullLocation(String locationString, String regex){
        String[] strings = locationString.split(regex);

        return new Location(Bukkit.getWorld(strings[0]),
                Double.parseDouble(strings[1]),
                Double.parseDouble(strings[2]),
                Double.parseDouble(strings[3]),
                Float.parseFloat(strings[4]),
                Float.parseFloat(strings[5])
        );
    }

    public static Location fromFullToNormal(Location fullLocation){
        return new Location(fullLocation.getWorld(), fullLocation.getBlockX(),
                fullLocation.getBlockY(), fullLocation.getBlockZ());
    }

    public static String fromNormalizedLocation(Location location, String regex){
        return String.join(", ", location.getWorld().getName(),
                String.valueOf(location.getBlockX()),
                String.valueOf(location.getBlockY()),
                String.valueOf(location.getBlockZ()));
    }

    public static String fromFullLocation(Location location, String regex){
        return String.join(", ", location.getWorld().getName(),
                String.valueOf(location.getBlockX()),
                String.valueOf(location.getBlockY()),
                String.valueOf(location.getBlockZ()),
                String.valueOf(location.getYaw()),
                String.valueOf(location.getPitch()));
    }

    public static boolean isOnTheLocation(Location loc1, Location playerLocation){
        double maxDiff = 3;

        if (playerLocation.getBlockX() == loc1.getBlockX() && playerLocation.getBlockZ() == loc1.getBlockZ()) {
            double diff = Math.abs(playerLocation.getY() - loc1.getY());

            return diff <= maxDiff;
        }

        return false;
    }


    public static ParsedLocation parseNormalLocation(Location location){
        return new ParsedLocation(location.getWorld().getName(),
                location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static ParsedLocation parseFullLocation(Location location){
        return new ParsedLocation(location.getWorld().getName(),
                location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch());
    }
}
