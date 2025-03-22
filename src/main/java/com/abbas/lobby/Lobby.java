package com.abbas.lobby;

import com.abbas.lobby.Listeners.PlayerBlockBreakEvent;
import com.abbas.lobby.Listeners.joinListener;
import com.abbas.lobby.Scoreobard.ScoreBoardListener;
import com.abbas.lobby.commands.AdminCommands.*;
import com.abbas.lobby.commands.PlayerCommands.*;
import com.abbas.lobby.menus.MenuGamesListener;
import com.abbas.lobby.menus.command.openMenuCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class Lobby extends JavaPlugin {
    private final Logger logger = getLogger();
    private static Lobby instance;
    public ArrayList<Player> vanish_list = new ArrayList<>();

    @Override
    public void onEnable() {
        logger.info("Lobby plugin Enabled");
        addcommands();
        listeners();
        instance = this;

    }

    public void addcommands(){
        getCommand("ping").setExecutor(new Ping());
        getCommand("help").setExecutor(new help());
        getCommand("kick").setExecutor(new Kick());
        getCommand("support").setExecutor(new Support());
        getCommand("vanish").setExecutor(new Vanish());
        getCommand("Discord").setExecutor(new Discord());
        getCommand("menu").setExecutor(new openMenuCommand());
    }

    public void listeners(){
        PluginManager p = Bukkit.getPluginManager();
        p.registerEvents(new ScoreBoardListener(this), this);
        p.registerEvents(new PlayerBlockBreakEvent(), this);
        p.registerEvents(new MenuGamesListener(), this);
        p.registerEvents(new joinListener(), this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        p.registerEvents(new MenuGamesListener(), this);
    }

    @Override
    public void onDisable() {
        logger.info("Lobby plugin Disabled");
        instance = null;

    }

    public static Lobby getInstance() {
        return instance;
    }

}
