package com.abbas.lobby.Listeners;

import com.abbas.lobby.API.EventsAPI.HungerAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Hunger implements Listener, HungerAPI {

    @EventHandler
    @Override
    public void handleHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
        event.setFoodLevel(getDefaultFoodLevel());
    }

    @EventHandler
    @Override
    public void handleDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @Override
    public int getDefaultFoodLevel() {
        return 20;
    }
}