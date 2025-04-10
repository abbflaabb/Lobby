package com.abbas.lobby.commands.PlayerCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.ChatColor;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;

import java.util.Arrays;

public class InformationCommand implements CommandExecutor {

    private static final String PERMISSION_INFO = "lobby.information";

    public InformationCommand() {
        setupConfig();
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("information")) {
            config.set("information.noPermission", "&cYou do not have permission to use this command!");

            config.set("information.rules", Arrays.asList(
                    "&7• No hacking or exploiting.",
                    "&7• Be respectful to all players.",
                    "&7• No advertising or spamming."
            ));

            config.set("information.social.discord", "https://discord.com/example");
            config.set("information.social.website", "https://example.com");
            config.set("information.social.store", "https://store.example.com");

            config.set("information.server.version", "1.20.1");
            config.set("information.server.gamemode", "Survival");

            config.set("information.staffContact", Arrays.asList(
                    "&7• Contact admins via /discord.",
                    "&7• Report issues in our Discord server.",
                    "&7• For urgent matters, email support@example.com."
            ));

            config.set("information.quickCommands", Arrays.asList(
                    "&7• &b/spawn - &fReturn to spawn",
                    "&7• &b/help - &fView help commands"
            ));

            Config.save();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = Config.getConfig();

        if (!sender.hasPermission(PERMISSION_INFO)) {
            sender.sendMessage(ColorUtils.translateColorCodes(config.getString("information.noPermission")));
            return true;
        }

        sendServerInformation(sender, config);
        return true;
    }

    private void sendServerInformation(CommandSender sender, FileConfiguration config) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GRAY + "━━━━━━━━━━ " + ChatColor.RED + "Server Information" + ChatColor.GRAY + " ━━━━━━━━━━");
        sender.sendMessage("");

        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Server Rules:");
        for (String rule : config.getStringList("information.rules")) {
            sender.sendMessage(ColorUtils.translateColorCodes("&7• " + rule));
        }
        sender.sendMessage("");

        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Social Media:");
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bDiscord: &f" + config.getString("information.social.discord")));
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bWebsite: &f" + config.getString("information.social.website")));
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bStore: &f" + config.getString("information.social.store")));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Server Information:");
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bVersion: &f" + config.getString("information.server.version")));
        sender.sendMessage(ColorUtils.translateColorCodes("&7• &bGame Mode: &f" + config.getString("information.server.gamemode")));
        sender.sendMessage("");

        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Staff Contact:");
        for (String contact : config.getStringList("information.staffContact")) {
            sender.sendMessage(ColorUtils.translateColorCodes(contact));
        }
        sender.sendMessage("");

        sender.sendMessage(ChatColor.RED + "➤ " + ChatColor.YELLOW + "Quick Commands:");
        for (String command : config.getStringList("information.quickCommands")) {
            sender.sendMessage(ColorUtils.translateColorCodes(command));
        }
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GRAY + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        sender.sendMessage("");
    }
}
