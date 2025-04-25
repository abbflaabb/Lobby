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

## License
This plugin is protected under copyright law. All rights reserved.

## Contact
- GitHub: [@abbflaabb](https://github.com/abbflaabb)
- Created: 2025-03-27 21:48:22
