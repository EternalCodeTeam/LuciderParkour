package com.duck.configuration.parsers;


import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ParsedLocation {
    private String world;
    private double x, y, z;
    private float pitch,yaw;

    public ParsedLocation(String world, double x, double y, double z, float pitch, float yaw) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public ParsedLocation(String world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location toNormalLocation(){
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public Location toFullLocation(){
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public ParsedLocation toParsedLocation(World world, double x, double y, double z, float yaw, float pitch){
        return ParsedLocation.builder()
                .world(world.getName())
                .x(x)
                .y(y)
                .z(z)
                .yaw(yaw)
                .pitch(pitch)
                .build();
    }

    public ParsedLocation toParsedLocation(World world, double x, double y, double z){
        return ParsedLocation.builder()
                .world(world.getName())
                .x(x)
                .y(y)
                .z(z)
                .build();
    }
}
