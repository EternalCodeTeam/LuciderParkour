package com.duck.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerFoodLevelChangeListener implements Listener {
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent foodLevelChangeEvent){
        if(foodLevelChangeEvent.getFoodLevel() < 20)
            foodLevelChangeEvent.setFoodLevel(20);
    }
}
