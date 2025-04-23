package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.API.ICommandAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Discord implements ICommandAPI {
    private static final String COMMAND_NAME = "discord";
    private static final String PERMISSION_NODE = "lobby.discord";

    public Discord() {
        setupConfig();
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("discord")) {
            config.set("discord.message-1", "&3&l &9&lDiscord");
            config.set("discord.message-2", "&3&l Join our discord server");
            config.set("discord.message-3", "&3&l www.discord.com/example.com");
            config.set("discord.noPermission", "&cYou don't have permission to use this command!");
            config.set("discord.playerOnly", "&cThis command can only be used by players!");
            Config.save();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(getCommandName())) {
            return false;
        }

        if (!hasPermission(sender)) {
            sendNoPermissionMessage(sender);
            return true;
        }

        if (isPlayerOnly() && !(sender instanceof Player)) {
            sendPlayerOnlyMessage(sender);
            return true;
        }

        sendDiscordMessages((Player) sender);
        return true;
    }

    private void sendDiscordMessages(Player player) {
        FileConfiguration config = Config.getConfig();
        String message1 = config.getString("discord.message-1");
        String message2 = config.getString("discord.message-2");
        String message3 = config.getString("discord.message-3");

        player.sendMessage(ColorUtils.translateColorCodes(message1));
        player.sendMessage(ColorUtils.translateColorCodes(message2));
        player.sendMessage(ColorUtils.translateColorCodes(message3));
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
        return "Shows Discord server information";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("discord.noPermission")));
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("discord.playerOnly")));
    }
}
