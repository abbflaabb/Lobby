package com.abbas.lobby;

import com.abbas.lobby.Listeners.*;
import com.abbas.lobby.Scoreobard.ScoreBoardConfig;
import com.abbas.lobby.Scoreobard.ScoreBoardListener;
import com.abbas.lobby.Utils.BanConfig;
import com.abbas.lobby.Utils.ChatFilterConfig;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.Utils.MuteConfig;
import com.abbas.lobby.Utils.UnbanConfig;
import com.abbas.lobby.Utils.WarnConfig;
import com.abbas.lobby.commands.AdminCommands.*;
import com.abbas.lobby.commands.PlayerCommands.*;
import com.abbas.lobby.commands.PlayerCommands.premuimCommands.FlyCommand;
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
    public ArrayList<Player> vanish_list = new ArrayList<>();

    @Override
    public void onEnable() {
        logger.info("Lobby plugin Enabled");
        addcommands();
        listeners();
        Config.setup();
        UnbanConfig.setupConfig();
        BanConfig.setupConfig();
        ScoreBoardConfig.setupConfig();
        WarnConfig.setupConfig();
        ChatFilterConfig.setup();
        MuteConfig.setupConfig();
    }

    public void addcommands(){
        getCommand("ping").setExecutor(new Ping());
        getCommand("help").setExecutor(new help());
        getCommand("kick").setExecutor(new Kick());
        getCommand("support").setExecutor(new Support());
        getCommand("vanish").setExecutor(new Vanish(this));
        getCommand("Discord").setExecutor(new Discord());
        getCommand("menu").setExecutor(new openMenuCommand());
        getCommand("lobby").setExecutor(new lobby());
        getCommand("SetSpawn").setExecutor(new SetSpawn());
        getCommand("Spawn").setExecutor(new Spawn());
        getCommand("Ban").setExecutor(new Ban());
        getCommand("UnBan").setExecutor(new Unban());
        getCommand("reloadconfig").setExecutor(new ReloadConfigs());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("warn").setExecutor(new WarnCommand());
        getCommand("information").setExecutor(new InformationCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("unmute").setExecutor(new UnmuteCommand());
        getCommand("checkban").setExecutor(new Ban());

    } // you dont Create Vanish Listener

    public void listeners(){
        PluginManager p = Bukkit.getPluginManager();
        p.registerEvents(new PlayerBlockBreakEvent(), this);
        p.registerEvents(new ScoreBoardListener(this), this);
        p.registerEvents(new MenuGamesListener(), this);
        p.registerEvents(new JoinListener(), this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        p.registerEvents(new MenuGamesListener(), this);
        p.registerEvents(new BlockPlace(), this);
        p.registerEvents(new DropEvent(), this);
        p.registerEvents(new ReSpawnListener(), this);
        p.registerEvents(new BanListener(), this);
        p.registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new MuteChatListener(), this);

    }



    @Override
    public void onDisable() {
        logger.info("Lobby plugin Disabled");

    }

}
