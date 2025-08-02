package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.API.ConfigAPI.ConfigCommandPath;
import com.abbas.lobby.API.MainAPIS.ICommandAPI;
import com.abbas.lobby.Lobby;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class hub implements ICommandAPI, TabCompleter {
    private static final String COMMAND_NAME = "lobby";
    private static final String PERMISSION_NODE = "lobby.lobby";
    private static final int COOLDOWN_SECONDS = 6;
    private final Lobby plugin;

    private Map<String, List<String>> commandCategories;
    private final Map<UUID, Long> cooldowns;

    public hub(Lobby plugin) {
        this.plugin = plugin;
        this.commandCategories = new HashMap<>();
        this.cooldowns = new HashMap<>();
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
            config.set(ConfigCommandPath.LOBBY_NO_PERMISSION, "&c⚠ You do not have permission to use this command!");
            config.set(ConfigCommandPath.LOBBY_PLAYER_ONLY, "&c⚠ Console cannot use this command!");
            config.set(ConfigCommandPath.LOBBY_COOLDOWN, "&c⚠ Please wait %cooldown_Time% seconds before using this command again!");
            config.set(ConfigCommandPath.LOBBY_HEADER, "&8&l≪ &6&lLobby Commands &8&l≫");
            config.set(ConfigCommandPath.LOBBY_FOOTER, "&8&l≪ &6&lEnd of Commands &8&l≫");
            config.set(ConfigCommandPath.LOBBY_GENERAL_COMMANDS, "&a&lGeneral Commands:");
            config.set(ConfigCommandPath.LOBBY_PREMIUM_COMMANDS, "&b&lPremium Commands:");
            config.set(ConfigCommandPath.LOBBY_ADMIN_COMMANDS, "&c&lAdmin Commands:");
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
        generalCommands.forEach((cmd, desc) -> formattedCommands.add("&7➤ &e/" + cmd + " &8- &7" + desc));
        formattedCommands.add("");
        formattedCommands.add("&b&lPremium Commands:");
        premiumCommands.forEach((cmd, desc) -> formattedCommands.add("&7➤ &b/" + cmd + " &8- &7" + desc));
        formattedCommands.add("");
        adminCommands.forEach((cmd, desc) -> formattedCommands.add("&7➤ &c/" + cmd + " &8- &7" + desc));

        config.set(ConfigCommandPath.LOBBY_COMMAND_LIST, formattedCommands);
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
        String message = Config.getConfig().getString(ConfigCommandPath.LOBBY_COOLDOWN, "&c⚠ Please wait %cooldown_Time% seconds before using this command again!");
        if (plugin.getPlaceholders() != null) {
            plugin.getPlaceholders().setCooldownTime(getRemainingCooldown(player));
            message = plugin.getPlaceholders().replacePlaceholders(message, player);
        } else {
            message = message.replace("%cooldown_Time%", String.valueOf(getRemainingCooldown(player)));
        }
        player.sendMessage(ColorUtils.translateColorCodes(message));
    }

    private void displayCommands(Player player) {
        FileConfiguration config = Config.getConfig();
        List<String> commands = config.getStringList(ConfigCommandPath.LOBBY_COMMAND_LIST);

        player.sendMessage(ColorUtils.translateColorCodes(config.getString(ConfigCommandPath.LOBBY_HEADER)));
        player.sendMessage("");

        for (String command : commands) {
            if (command.contains("/fly") && !player.hasPermission("lobby.fly")) continue;
            player.sendMessage(ColorUtils.translateColorCodes(command));
        }

        player.sendMessage("");
        player.sendMessage(ColorUtils.translateColorCodes(config.getString(ConfigCommandPath.LOBBY_FOOTER)));
    }

    private boolean isOnCooldown(Player player) {
        UUID uuid = player.getUniqueId();
        return cooldowns.containsKey(uuid) && cooldowns.get(uuid) > System.currentTimeMillis();
    }

    private void setCooldown(Player player) {
        UUID uuid = player.getUniqueId();
        cooldowns.put(uuid, System.currentTimeMillis() + (COOLDOWN_SECONDS * 1000L));
    }

    private int getRemainingCooldown(Player player) {
        UUID uuid = player.getUniqueId();
        long cooldownEnd = cooldowns.getOrDefault(uuid, 0L);
        long remaining = (cooldownEnd - System.currentTimeMillis()) / 1000;
        return (int) Math.max(0, remaining);
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
                Config.getConfig().getString(ConfigCommandPath.LOBBY_NO_PERMISSION)));
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString(ConfigCommandPath.LOBBY_PLAYER_ONLY)));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase(getCommandName())) return null;
        return new ArrayList<>();
    }
}
