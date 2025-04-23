package com.abbas.lobby;

import com.abbas.lobby.API.*;
import com.abbas.lobby.BossBar.BossBar;
import com.abbas.lobby.BossBar.BossBarConfig;
import com.abbas.lobby.BossBar.BossBarListener;
import com.abbas.lobby.BossBar.BossBarReload;
import com.abbas.lobby.Placeholders.Placeholders;
import com.abbas.lobby.Scoreboard.*;
import com.abbas.lobby.TeleportBow.TeleportBow;
import com.abbas.lobby.Listeners.*;
import com.abbas.lobby.SubTitle.SubTitle;
import com.abbas.lobby.SubTitle.SubTitleListener;
import com.abbas.lobby.TeleportBow.TeleportBowListener;
import com.abbas.lobby.Utils.*;
import com.abbas.lobby.commands.AdminCommands.*;
import com.abbas.lobby.commands.PlayerCommands.*;
import com.abbas.lobby.API.ICommandAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class Lobby extends JavaPlugin {
    private final Logger logger = getLogger();
    private final List<ICommandAPI> commands = new ArrayList<>();
    public ArrayList<Player> vanish_list = new ArrayList<>();
    public BossBar bossBar;
    private Placeholders placeholders;
    private ISubTitle subTitle;
    private static Lobby instance;
    private ITeleportAPI teleportAPI;
    private IScoreboard scoreboard;
    private ILuckPerms luckPerms;
    private ICommandAPI commandAPI;
    @Override
    public void onEnable() {
        logger.info("╔════════════════════════════════════╗");
        logger.info("║        Lobby Plugin v" + getDescription().getVersion() + "║");
        logger.info("║------------------------------------║");

        initializeServices();
        registerAPIServices();
        registerCommands();
        registerListeners();
        setupConfigs();

        logStartupInfo();
    }

    private void initializeServices() {
        this.bossBar = new BossBar(this);
        BossBarAPI.initialize(this);
        this.placeholders = new Placeholders(this.luckPerms);
        this.subTitle = new SubTitle();
        instance = this;
        this.teleportAPI = new TeleportBow();
        this.teleportAPI.setupConfig();
        this.luckPerms = new LuckPermsRank();
        this.luckPerms.setup();
        this.scoreboard = new ScoreBoardManager(this);
    }

    private void registerAPIServices() {
        getServer().getServicesManager().register(
                ISubTitle.class,
                this.subTitle,
                this,
                ServicePriority.Normal
        );
    }

    private void registerCommands() {
        registerAPICommand(new FlyCommand());
        registerAPICommand(new hub());
        registerAPICommand(new InformationCommand());
        registerAPICommand(new Ping());
        registerAPICommand(new Spawn());
        registerAPICommand(new Support());
        registerAPICommand(new Ban());
        registerAPICommand(new Kick());
        registerAPICommand(new MuteCommand());
        registerAPICommand(new ReloadConfigs());
        registerAPICommand(new SetSpawn());
        registerAPICommand(new Vanish(this));
        registerAPICommand(new Unban());
        registerAPICommand(new WarnCommand());
        registerAPICommand(new UnmuteCommand());
        registerAPICommand(new BossBarReload(this));
        registerAPICommand(new ScoreBoardCommand(this.scoreboard));
        hub lobbyCommand = new hub();
        getCommand("lobby").setExecutor(lobbyCommand);
        getCommand("lobby").setTabCompleter(lobbyCommand);
        getCommand("Discord").setExecutor(new Discord());
    }

    private void registerAPICommand(ICommandAPI command) {
        commands.add(command);
        getCommand(command.getCommandName()).setExecutor(command);
        logger.info("Registered API command: " + command.getCommandName());
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PlayerBlockBreakEvent(), this);
        pm.registerEvents(new ScoreBoardListener(this), this);
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new BlockPlace(), this);
        pm.registerEvents(new DropEvent(), this);
        pm.registerEvents(new ReSpawnListener(), this);
        pm.registerEvents(new BanListener(), this);
        pm.registerEvents(new MuteChatListener(), this);
        pm.registerEvents(new Hunger(), this);
        pm.registerEvents(new SubTitleListener(this.subTitle, this), this);
        pm.registerEvents(new TeleportBowListener(this), this);
        pm.registerEvents(new BossBarListener(), this);
    }

    private void setupConfigs() {
        Config.setup();
        UnbanConfig.setupConfig();
        BanConfig.setupConfig();
        ScoreBoardConfig.setupConfig();
        WarnConfig.setupConfig();
        MuteConfig.setupConfig();
        BossBarConfig.setup();
    }

    private void logStartupInfo() {
        String author = getDescription().getAuthors().isEmpty() ? "Unknown" : getDescription().getAuthors().get(0);

        logger.info("║  Author: " + author + "                     ║");
        logger.info("║  Server: " + getServer().getBukkitVersion() + "       ║");
        logger.info("║  Dependencies:                     ║");
        logger.info("║    - PlaceholderAPI                ║");
        logger.info("║    - LuckPerms                     ║");
        logger.info("║    - Vault                         ║");
        logger.info("╚════════════════════════════════════╝");
    }

    @Override
    public void onDisable() {
        if (bossBar != null) {
            bossBar.close();
        }

        BossBarAPI.shutdown();
        getServer().getServicesManager().unregisterAll(this);

        getServer().getOnlinePlayers().forEach(player ->
                this.subTitle.clearTitle(player)
        );

        logger.info("╔════════════════════════════════════╗");
        logger.info("║      Shutting down Lobby...        ║");
        logger.info("║------------------------------------║");
        logger.info("║  Saving configurations...          ║");
        logger.info("║  Cleaning up resources...          ║");
        logger.info("║  Plugin disabled successfully      ║");
        logger.info("╚════════════════════════════════════╝");
    }


    public List<ICommandAPI> getCommands() {
        return commands;
    }

    public Placeholders getPlaceholders() {
        return placeholders;
    }

    public ISubTitle getSubTitleAPI() {
        return this.subTitle;
    }

    public IBossBar getBossBarAPI() {
        return BossBarAPI.getBossBar();
    }

    public ITeleportAPI getTeleportAPI() {
        return teleportAPI;
    }

    public IScoreboard getScoreboard() {
        return scoreboard;
    }

    public ILuckPerms getLuckPerms() {
        return luckPerms;
    }

    public static Lobby getInstance() {
        return instance;
    }
}
