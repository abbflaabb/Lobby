package com.abbas.lobby;

import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.commands.AdminCommand.*;
import com.abbas.lobby.commands.PlayerCommand.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class Lobby extends JavaPlugin {
    private final Logger logger = getLogger();

    public ArrayList<Player> vanish_list = new ArrayList<>();

    @Override
    public void onEnable() {
        logger.info("Lobby plugin Enabled");
        addcommands();
        listeners();

        Config.setup();
        if (!Config.getConfig().contains("prefix")) {
            Config.getConfig().set("prefix", "&8[&6Lobby&8]");
            Config.save();
        }
    }

    public void addcommands(){
        getCommand("ping").setExecutor(new Ping());
        getCommand("help").setExecutor(new help());
        getCommand("kick").setExecutor(new Kick());
        getCommand("support").setExecutor(new Support());
    }

    public void listeners(){

    }

    @Override
    public void onDisable() {
        logger.info("Lobby plugin Disabled");
    }

}
