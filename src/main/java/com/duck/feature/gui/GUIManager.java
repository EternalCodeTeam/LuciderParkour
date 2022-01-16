package com.duck.feature.gui;

import com.duck.feature.gui.impl.ParkourCategoryMenu;
import com.duck.feature.gui.impl.ParkourMainMenu;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class GUIManager {
    private final ParkourMainMenu parkourMainMenu = new ParkourMainMenu("&3Test inventory");

    public ParkourMainMenu getParkourMainMenu() {
        return parkourMainMenu;
    }

}
