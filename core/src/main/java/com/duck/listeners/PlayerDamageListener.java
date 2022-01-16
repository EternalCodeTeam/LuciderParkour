package com.duck.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onPlayerAttack(EntityDamageEvent event){
        if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
            event.setCancelled(true);
    }
}
