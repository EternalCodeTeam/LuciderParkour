package com.duck.feature.gui;

import com.duck.feature.gui.impl.ParkourMainMenu;

public class GUIManager {
    private final ParkourMainMenu parkourMainMenu = new ParkourMainMenu("&3Test inventory");

    public ParkourMainMenu getParkourMainMenu() {
        return parkourMainMenu;
    }

}
