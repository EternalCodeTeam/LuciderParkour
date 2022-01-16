package com.duck.parkour;

import dev.triumphteam.gui.guis.Gui;
import lombok.*;
import org.bukkit.Material;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ParkourCategory {
    private String name;
    private int guiIndex;
    private Material guiMaterial;
    private Material parkourMaterial;

    private String displayName;
    private String[] description;

    private double xpReward;
    private int requiredLevel;

    private boolean permissionRequired;
    private String permissionName;

    private List<Integer> ids;
}
