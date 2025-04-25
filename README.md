# Lobby Plugin

## Description
This is a custom Lobby plugin designed to enhance the server experience with essential lobby functionality, including player management, teleportation systems, and server navigation features. The plugin provides a seamless hub experience for players connecting to the server.

## Progress
<div align="center">

![Java](https://img.shields.io/badge/Java-35%25-orange?style=for-the-badge&logo=java&logoColor=white)
![Last Updated](https://img.shields.io/badge/Last%20Updated-2025--03--27-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow?style=for-the-badge)

</div>

## Features
### **Admin Commands**
- **Ban** - Bans a player from the server, preventing them from joining again
- **Kick** - Kicks a player from the server without banning them
- **ReloadConfigs** - Reloads plugin configurations dynamically without requiring a server restart
- **SetSpawn** - Sets the global spawn point for players
- **Unban** - Unbans a previously banned player, allowing them to rejoin
- **Vanish** - Enables vanish mode, making admins invisible to other players

### **Player Commands**
- **Discord** - Provides a clickable link to the server's official Discord
- **help** - Displays an in-game help menu with available commands
- **lobby** - Teleports the player to the main lobby area
- **Ping** - Displays the player's ping in milliseconds
- **Spawn** - Instantly teleports the player to the spawn point
- **Support** - Provides contact information for server support

### **Listeners**
- **BanListener** - Automatically triggers events related to player bans
- **JoinListener** - Manages welcome messages and player data loading on join
- **PlayerProtectionListener** - Prevents PvP and damage in lobby areas
- **ServerSelectorListener** - Handles server selection menu interactions
- **SpawnProtectionListener** - Protects the lobby area from modifications


#### Scoreboard
- **ScoreBoardConfig** - Configurable scoreboard elements and formatting
- **ScoreBoardListener** - Dynamic scoreboard updates
- **ScoreBoardManager** - Manages scoreboard content and display

## Installation
1. Download the latest version of the plugin JAR file
2. Place it in your server's `plugins/` directory
3. Restart the server to generate configuration files
4. Configure the settings in `config.yml` to match your server needs
5. Use `/reloadConfig` to apply changes without server restart

## Dependencies
Required for optimal functionality:
- **BungeeCord/Velocity** - For cross-server functionality
- **PlaceholderAPI** - For advanced placeholder support
- **Vault** - For economy integration (if enabled)



##How To Use API
```java Example For Use IPlaceholders
import com.abbas.lobby.API.IPlaceholders;
import org.bukkit.entity.Player;

public class CustomPlaceholder {
    private final IPlaceholders placeholderManager;

    public CustomPlaceholder(IPlaceholders placeholderManager) {
        this.placeholderManager = placeholderManager;
        registerPlaceholders();
    }

    private void registerPlaceholders() {
        // Register health placeholder
        placeholderManager.addPlaceholder("%player_health%",
                player -> String.valueOf(player.getHealth()));

        // You can add more custom placeholders here
        placeholderManager.addPlaceholder("%player_food%",
                player -> String.valueOf(player.getFoodLevel()));
    }

    // Example usage method
    public String processText(String text, Player player) {
        return placeholderManager.replacePlaceholders(text, player);
    }
}

Example For Use Placeholder in Listener
package UseAPI;

import com.abbas.lobby.Lobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CustomPlaceholderExample implements Listener {
    private final CustomPlaceholder customPlaceholder;

    public CustomPlaceholderExample(Lobby plugin) {
        this.customPlaceholder = new CustomPlaceholder(plugin.getPlaceholders());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String message = "Health: %player_health% Food: %player_food%";
        String processed = customPlaceholder.processText(message, player);
        player.sendMessage(processed);
    }
}

##Example For Use ICommandAPI
package UseAPI;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.API.ICommandAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ExampleCommand implements ICommandAPI {
    private static final String COMMAND_NAME = "example";
    private static final String PERMISSION_NODE = "lobby.example";

    public ExampleCommand() {
        setupConfig();
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
        player.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("example.message")));
        return true;
    }

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("example")) {
            config.set("example.noPermission", "&c⚠ You do not have permission to use this command!");
            config.set("example.playerOnly", "&c⚠ Console cannot use this command!");
            config.set("example.message", "&aThis is an example command message!");
            Config.save();
        }
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
        return "An example command showing ICommandAPI implementation";
    }

    @Override
    public void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("example.noPermission")));
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void sendPlayerOnlyMessage(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(
                Config.getConfig().getString("example.playerOnly")));
    }
}
##Example For Use ILuckPerms
package UseAPI;

import com.abbas.lobby.API.ILuckPerms;
import com.abbas.lobby.Lobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LuckPermsExample implements Listener {
    private final ILuckPerms luckPerms;

    public LuckPermsExample(Lobby plugin) {
        this.luckPerms = plugin.getLuckPerms();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        displayPlayerInfo(player);
    }

    private void displayPlayerInfo(Player player) {
        String rank = luckPerms.getPlayerRank(player);

        String prefix = luckPerms.getPlayerPrefix(player);
        if (prefix == null) {
            prefix = "§7";
        }

        player.sendMessage("§6=== Your Rank Info ===");
        player.sendMessage("§eRank: §f" + rank);
        player.sendMessage("§ePrefix: " + prefix);

        if (player.hasPermission("lobby.vip")) {
            player.sendMessage("§aYou have VIP privileges!");
        }

        if (rank.equalsIgnoreCase("admin")) {
            player.sendMessage("§cWelcome back, Administrator!");
        }
    }

    public boolean isPlayerInGroup(Player player, String group) {
        String rank = luckPerms.getPlayerRank(player);
        return rank.equalsIgnoreCase(group);
    }

    public String getFormattedName(Player player) {
        String prefix = luckPerms.getPlayerPrefix(player);
        if (prefix == null) {
            prefix = "§7";
        }
        return prefix + player.getName();
    }
}
##Example For Use IScoreboard
package UseAPI;

import com.abbas.lobby.API.ILuckPerms;
import com.abbas.lobby.API.IScoreboard;
import com.abbas.lobby.Lobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ScoreboardExample implements Listener, CommandExecutor {
    private final IScoreboard scoreboard;
    private final ILuckPerms luckPerms;

    public ScoreboardExample(Lobby plugin) {
        this.scoreboard = plugin.getScoreboard();
        this.luckPerms = plugin.getLuckPerms();

        // Register this as listener and command executor
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getCommand("togglescoreboard").setExecutor(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        setupPlayerScoreboard(player);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        toggleScoreboard(player);
        return true;
    }

    private void setupPlayerScoreboard(Player player) {
        // Set player's scoreboard
        scoreboard.setScoreboard(player);

        // Get player's rank and prefix
        String rank = luckPerms.getPlayerRank(player);
        String prefix = luckPerms.getPlayerPrefix(player);

        // Auto-hide scoreboard if player has permission
        if (player.hasPermission("lobby.scoreboard.autohide")) {
            scoreboard.toggleScoreboardState(player.getUniqueId());
        }

        // Update all scoreboards
        scoreboard.updateScoreboard();
    }

    private void toggleScoreboard(Player player) {
        if (!player.hasPermission("lobby.scoreboard")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return;
        }

        scoreboard.toggleScoreboardState(player.getUniqueId());
        boolean isHidden = scoreboard.isScoreboardHidden(player.getUniqueId());

        if (isHidden) {
            player.sendMessage("§cScoreboard hidden!");
        } else {
            scoreboard.setScoreboard(player);
            player.sendMessage("§aScoreboard shown!");
        }
    }
}

## Example for use ISubTitle
package UseAPI;

import com.abbas.lobby.API.ISubTitle;
import com.abbas.lobby.Lobby;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class SubTitleExample implements Listener {
    private final ISubTitle subTitle;

    public SubTitleExample(Plugin plugin) {
        Lobby lobby = (Lobby) plugin.getServer().getPluginManager().getPlugin("Lobby");
        this.subTitle = lobby.getSubTitleAPI();
    }

    public void showWelcomeTitle(Player player) {
        subTitle.sendTitle(player, "welcome");
    }

    public void clearPlayerTitle(Player player) {
        subTitle.clearTitle(player);
    }
}
## Example For Use ITeleportAPI
package UseAPI;

import com.abbas.lobby.Lobby;
import com.abbas.lobby.API.ITeleportAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class TeleportBowExample implements Listener {
    private final Lobby plugin;

    public TeleportBowExample(Lobby plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/givebow")) {
            event.setCancelled(true);
            Player player = event.getPlayer();

            // Use the API through the main plugin class
            ITeleportAPI teleportAPI = plugin.getTeleportAPI();

            if (teleportAPI.isEnabled()) {
                teleportAPI.giveTeleportBowItems(player);
                player.sendMessage("You received a teleport bow!");
            } else {
                player.sendMessage("Teleport bow is currently disabled!");
            }
        }
    }
}
```
## License
This plugin is protected under copyright law. All rights reserved.

## Contact
- GitHub: [@abbflaabb](https://github.com/abbflaabb)
- Created: 2025-03-27 21:48:22
