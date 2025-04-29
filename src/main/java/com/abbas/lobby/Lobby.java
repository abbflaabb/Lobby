package com.abbas.lobby;

import com.abbas.lobby.API.EventsAPI.*;
import com.abbas.lobby.API.MainAPIS.*;
import com.abbas.lobby.Placeholders.Placeholders;
import com.abbas.lobby.PlayerVisibility.PlayerVisibility;
import com.abbas.lobby.Scoreboard.*;
import com.abbas.lobby.TeleportBow.TeleportBow;
import com.abbas.lobby.Listeners.*;
import com.abbas.lobby.SubTitle.SubTitle;
import com.abbas.lobby.SubTitle.SubTitleListener;
import com.abbas.lobby.TeleportBow.TeleportBowListener;
import com.abbas.lobby.Utils.*;
import com.abbas.lobby.commands.AdminCommands.*;
import com.abbas.lobby.commands.PlayerCommands.*;
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
    private Placeholders placeholders;
    private ISubTitle subTitle;
    private static Lobby instance;
    private ITeleportAPI teleportAPI;
    private IScoreboard scoreboard;
    private ILuckPerms luckPerms;
    private ICommandAPI commandAPI;
    private BanListenerAPI banListenerAPI;
    private BlockPlaceAPI blockPlaceAPI;
    private DropAPI dropAPI;
    private HungerAPI hungerAPI;
    private JoinListenerAPI joinListenerAPI;
    private MuteChatAPI muteChatAPI;
    private BlockBreakAPI blockBreakAPI;
    private RespawnAPI respawnAPI;
    private static IVisibilityAPI visibilityAPI;


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
        this.placeholders = new Placeholders(this.luckPerms);
        this.subTitle = new SubTitle();
        instance = this;
        this.teleportAPI = new TeleportBow();
        this.teleportAPI.setupConfig();
        this.luckPerms = new LuckPermsRank();
        this.luckPerms.setup();
        this.scoreboard = new ScoreBoardManager(this);
        this.subTitle = new SubTitleListener(new SubTitle(), this);
        registerAPIs();
        visibilityAPI = new VisibilityAPI();

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
    private void registerAPIs() {
        banListenerAPI = new BanListener();
        blockPlaceAPI = new BlockPlace();
        dropAPI = new DropEvent();
        hungerAPI = new Hunger();
        joinListenerAPI = new JoinListener();
        muteChatAPI = new MuteChatListener();
        blockBreakAPI = new PlayerBlockBreakEvent();
        respawnAPI = new ReSpawnListener();
        teleportAPI = new TeleportBowListener(this);
    }
    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new ScoreBoardListener(this), this);
        pm.registerEvents((TeleportBowListener) teleportAPI, this);
        pm.registerEvents((SubTitleListener) this.subTitle, this);
        pm.registerEvents((BanListener) banListenerAPI, this);
        pm.registerEvents((BlockPlace) blockPlaceAPI, this);
        pm.registerEvents((DropEvent) dropAPI, this);
        pm.registerEvents((Hunger) hungerAPI, this);
        pm.registerEvents((JoinListener) joinListenerAPI, this);
        pm.registerEvents((MuteChatListener) muteChatAPI, this);
        pm.registerEvents((PlayerBlockBreakEvent) blockBreakAPI, this);
        pm.registerEvents((ReSpawnListener) respawnAPI, this);
        getServer().getPluginManager().registerEvents(new PlayerVisibility(this), this);
    }

    private void setupConfigs() {
        Config.setup();
        UnbanConfig.setupConfig();
        BanConfig.setupConfig();
        ScoreBoardConfig.setupConfig();
        WarnConfig.setupConfig();
        MuteConfig.setupConfig();
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


    public ITeleportAPI getTeleportAPI() {
        return teleportAPI;
    }

    public IScoreboard getScoreboard() {
        return scoreboard;
    }

    public ILuckPerms getLuckPerms() {
        return luckPerms;
    }
    public BanListenerAPI getBanListenerAPI() {
        return banListenerAPI;
    }

    public BlockBreakAPI getBlockBreakAPI() {
        return blockBreakAPI;
    }

    public BlockPlaceAPI getBlockPlaceAPI() {
        return blockPlaceAPI;
    }

    public DropAPI getDropAPI() {
        return dropAPI;
    }
    public HungerAPI getHungerAPI() {
        return hungerAPI;
    }
    public JoinListenerAPI getJoinListenerAPI() {
        return joinListenerAPI;
    }
    public MuteChatAPI getMuteChatAPI() {
        return muteChatAPI;
    }
    public RespawnAPI getRespawnAPI() {
        return respawnAPI;
    }
    public ICommandAPI getCommandAPI() {
        return commandAPI;
    }

    public static IVisibilityAPI getVisibilityAPI() {
        return visibilityAPI;
    }

    public static Lobby getInstance() {
        return instance;
    }
}