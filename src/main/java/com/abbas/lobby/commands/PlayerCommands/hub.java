package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.API.ICommandAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class hub implements ICommandAPI, TabCompleter {
    private static final String COMMAND_NAME = "lobby";
    private static final String PERMISSION_NODE = "lobby.lobby";
    private static final int COOLDOWN_SECONDS = 10;

    private Map<String, List<String>> commandCategories;
    private Set<UUID> cooldowns;



    public hub() {
        this.commandCategories = new HashMap<>();
        this.cooldowns = new HashSet<>();
        setupConfig();
        initializeCommandCategories();
    }

    private void initializeCommandCategories() {
        commandCategories.put("general", Arrays.asList("lobby", "ping", "support", "information", "spawn", "sb", "help", "discord"));
        commandCategories.put("premium", Arrays.asList("fly", "fly high"));
        commandCategories.put("admin", Arrays.asList("ban", "kick", "mute", "reload", "setspawn", "unban", "unmute", "vanish", "warn", "bossbarreload"));
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("lobby")) {
            config.set("lobby.noPermission", "&c⚠ You do not have permission to use this command!");
            config.set("lobby.playerOnly", "&c⚠ Console cannot use this command!");
            config.set("lobby.cooldown", "&c⚠ Please wait %time% seconds before using this command again!");
            config.set("lobby.header", "&8&l≪ &6&lLobby Commands &8&l≫");
            config.set("lobby.footer", "&8&l≪ &6&lEnd of Commands &8&l≫");
            config.set("lobby.generalCommands", "&a&lGeneral Commands:");
            config.set("lobby.premiumCommands", "&b&lPremium Commands:");
            config.set("lobby.adminCommands", "&c&lAdmin Commands:");

            setupCommandList(config);
            Config.save();
        }
    }

    private void setupCommandList(FileConfiguration config) {
        Map<String, String> generalCommands = new LinkedHashMap<>();
        generalCommands.put("lobby", "Main command for the lobby");
        generalCommands.put("ping", "Check your ping");
        generalCommands.put("support", "Get support from staff");
        generalCommands.put("information", "Get server information");
        generalCommands.put("spawn", "Teleport to spawn");
        generalCommands.put("sb", "Toggle scoreboard");
        generalCommands.put("help", "Show help commands");
        generalCommands.put("discord", "Join our Discord server");

        Map<String, String> premiumCommands = new LinkedHashMap<>();
        premiumCommands.put("fly", "Fly around the lobby");
        premiumCommands.put("fly high", "Fly with increased speed");

        Map<String, String> adminCommands = new LinkedHashMap<>();
        adminCommands.put("ban", "Ban a player");
        adminCommands.put("kick", "Kick a player");
        adminCommands.put("mute", "Mute a player");
        adminCommands.put("reload", "Reload the plugin");
        adminCommands.put("setspawn", "Set the spawn location");
        adminCommands.put("unban", "Unban a player");
        adminCommands.put("unmute", "Unmute a player");
        adminCommands.put("vanish", "Toggle visibility");
        adminCommands.put("warn", "Warn a player");


        List<String> formattedCommands = new ArrayList<>();

        formattedCommands.add("&6&lGeneral Commands:");
        generalCommands.forEach((cmd, desc) ->
                formattedCommands.add("&7➤ &e/" + cmd + " &8- &7" + desc));

        formattedCommands.add("");
        formattedCommands.add("&b&lPremium Commands:");
        premiumCommands.forEach((cmd, desc) ->
                formattedCommands.add("&7➤ &b/" + cmd + " &8- &7" + desc));

        formattedCommands.add("");
        adminCommands.forEach((cmd, desc) ->
                formattedCommands.add("&7➤ &c/" + cmd + " &8- &7" + desc));
        config.set("lobby.commandList", formattedCommands);
        Config.save();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(getCommandName())) {
            return false;
        }

        if (isPlayerOnly() && !(sender instanceof Player)) {
            sendPlayerOnlyMessage(sender);
            return true;
        }

        if (!hasPermission(sender)) {
            sendNoPermissionMessage(sender);
            return true;
        }

        Player player = (Player) sender;

        if (isOnCooldown(player)) {
            sendCooldownMessage(player);
            return true;
        }

        displayCommands(player);
        setCooldown(player);

        return true;
    }

    private void sendCooldownMessage(Player player) {
        String message = Config.getConfig().getString("lobby.cooldown")
                .replace("%time%", String.valueOf(getRemainingCooldown(player)));
        player.sendMessage(ColorUtils.translateColorCodes(message));
    }

    private void displayCommands(Player player) {
        FileConfiguration config = Config.getConfig();
        List<String> commands = config.getStringList("lobby.commandList");

        player.sendMessage(ColorUtils.translateColorCodes("&8&l≪ &6&lAvailable Commands &8&l≫"));
        player.sendMessage("");

        for (String command : commands) {
            if (command.contains("/fly") && !player.hasPermission("lobby.fly")) continue;
            player.sendMessage(ColorUtils.translateColorCodes(command));
        }

        player.sendMessage("");
        player.sendMessage(ColorUtils.translateColorCodes("&8&l≪ &6&lEnd of Commands &8&l≫"));
    }

    private boolean isOnCooldown(Player player) {
        return cooldowns.contains(player.getUniqueId());
    }

    private void setCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        cooldowns.add(playerId);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                cooldowns.remove(playerId);
            }
        }, COOLDOWN_SECONDS * 1000L);
    }

    private int getRemainingCooldown(Player player) {
        return COOLDOWN_SECONDS;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermissionNode());
    }

    @Override
    public String getPermissionNode() {
        return PERMISSION_NODE;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String getDescription() {
        return "Display all available commands";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("lobby.noPermission")));
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("lobby.playerOnly")));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase(getCommandName())) return null;
        return new ArrayList<>();
    }
}