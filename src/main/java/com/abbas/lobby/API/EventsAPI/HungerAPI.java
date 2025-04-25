package com.abbas.lobby.API.EventsAPI;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public interface HungerAPI {

    void handleHunger(FoodLevelChangeEvent event);
    void handleDamage(EntityDamageEvent event);
    int getDefaultFoodLevel();
}